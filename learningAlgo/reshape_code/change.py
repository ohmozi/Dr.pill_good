from PIL import Image, ImageOps, ImageFilter
import os,sys
import glob
import cv2
import numpy as np
import matplotlib.pyplot as plt
#attractionFolderList = ['갈색', '검정','남색','노랑', '보라','분홍','빨강', '연두', '자주', '주황', '청록', '초록', '파랑','하양','회색']
#attractionFolderList = ['회색']

#attractionFolderList = ['마름모형','사각형','삼각형','오각형','원형','육각형', '장방형','타원형','팔각형']
attractionFolderList = ['빨강']

for attractionFolder in attractionFolderList:
    #image_dir = "C:/Users/Jihun/Desktop/훈련데이터/color/"+attractionFolder+"/"
    image_dir = "C:/Users/Jihun/Desktop/ABC/"
    #target_resize_dir = "C:/Users/Jihun/Desktop/보내기/"+attractionFolder+"/"
    target_resize_dir = "C:/Users/Jihun/Desktop/Blue/"

    if not os.path.isdir(target_resize_dir):
        os.makedirs(target_resize_dir)

    files = glob.glob(image_dir+"*")
    print(len(files))
    count = 1;
    #size = (32, 32)
    for file in files:
        #im = cv2.imread(file,cv2.IMREAD_COLOR)
        #im =cv2.resize(im, dsize=(400, 224), interpolation=cv2.INTER_AREA)
        #im = cv2.cvtColor(im,cv2.COLOR_BGR2GRAY)
        #im = cv2.GaussianBlur(im,(3,3),0)
        #im =cv2.adaptiveThreshold(im,255,cv2.ADAPTIVE_THRESH_MEAN_C,cv2.THRESH_BINARY,17,6)
        #im = cv2.Canny(im,100,100)

        im = Image.open(file)
        im = im.convert('RGB')
        im = im.resize((200,100))
        im = im.crop((12,5,100,80))
        im = im.resize((64,64))

        #im = im.filter(ImageFilter.MaxFilter) #GaussianBlur, BLUR, MaxFilter ...
        #print("i: ", count, im.format, im.size, im.mode, file.split("/")[-1])
        count+=1


        #print(target_resize_dir+file.split("\\")[-1])
        #cv2.imwrite(target_resize_dir+file.split("\\")[-1],im)
        #cv2.imwrite(os.path.join(image_dir,file.split("\\")[-1]),im)


        #im = ImageOps.fit(im, size, Image.ANTIALIAS, 0, (0.5, 0.5))
        #im.save(target_resize_dir+file.split("\\")[-1]+".jpg", quality=100)
        im.save(target_resize_dir+file.split("\\")[-1]+".png", quality=100)
