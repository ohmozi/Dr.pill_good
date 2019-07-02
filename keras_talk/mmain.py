# -*- coding: UTF-8 -*-

from flask import Flask, render_template, request
from flask import redirect
from werkzeug import secure_filename
import base64
import keras_predict
import numpy as np
import os
import pymysql
from PIL import Image, ImageEnhance
import json
import cv2

code=tuple()

app = Flask(__name__,template_folder='/home/ubuntu/keras_talk/Templates')

#
def get_connection() :
    conn = pymysql.connect(host='127.0.0.1', user='root',
            password='dhahwl', db='pill_db', charset='utf8')        #DB설정필요

    return conn


#test code
@app.route('/upload')
def render_file():
    return render_template('upload.html')

@app.route('/test', methods=["POST"])
def test():
    global code
    getj = request.get_json()["image"]
    mark = request.get_json()["name"]
    print(getj)
    image = json.dumps(getj)
    img=base64.b64decode(getj)
    print(type(img))
    with open('/home/ubuntu/keras_talk/test3/test_data/image.png','wb') as f:
        f.write(img)
    f.close()
    img = cv2.imread('/home/ubuntu/keras_talk/test3/test_data/image.png',cv2.IMREAD_COLOR)
    final = Image.fromarray(img)
    final = ImageEnhance.Contrast(final).enhance(1.0)
    final = np.array(final)
    cv2.imwrite('/home/ubuntu/keras_talk/test3/test_data/image.png',final)
    color = keras_predict.color_predict()
    shape = keras_predict.shape_predict()
    #os.remove("/home/ubuntu/keras_talk/test3/test_data/image.jpg")
    if color =="orange":
        color = "주황"
    elif color =="red":
        color = "갈색"
    elif color =="white":
        color = "하양"
    elif color =="blue":
        color = "파랑"
    if shape =="circle":
        shape = "원형"
    elif shape =="elipse":
        shape = "타원형"   #

    code = get_content(mark,shape,color)
    #print(mark)
    #print(code)
    return "OK"

@app.route('/view')
def Vview():
    global code
    #print(code)
    return render_template('view.html',data=code)


def get_content(mark, shape, color) :
    #
    sql = '''select pill_code,pill_name,pill_company, pill_mark1, pill_mark2,pill_shape, pill_color1, pill_color2, pill_cate, pill_image
             from pill_table1
             where (pill_mark1 LIKE %s or pill_mark2 LIKE %s) and (pill_shape = %s or pill_shape = %s) and (pill_color1 = %s or pill_color2 = %s) LIMIT 30
            '''

    #
    conn = get_connection()
    #
    cursor = conn.cursor()
    if shape == "타원형":
        shape1 = "장방형"
    else :
        shape1 = "원형"
    cursor.execute(sql, ('%'+mark+'%', '%'+mark+'%', shape, shape1, color,color))

    result = cursor.fetchall()
    '''for data in result:
        print(data[0],data[1],data[2],data[3],data[4],data[5])
        result_dic['pill_code']=data[0]
        result_dic['pill_mark1']=data[1]
        result_dic['pill_shape']=data[2]
        result_dic['pill_color1']=data[3]
        result_dic['pill_color2']=data[4]
        result_dic['pill_image']=data[5]'''#
    print("\n\n\n")

    conn.close()

    return result
if __name__ == '__main__':
    app.run(host="0.0.0.0",port=3000)
