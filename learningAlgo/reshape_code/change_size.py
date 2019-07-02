from PIL import Image, ImageOps, ImageFilter
import os,sys
import glob
import cv2
import numpy as np
import matplotlib.pyplot as plt


image_dir = "C:/Users/Jihun/Desktop/ZZZ/a/"

#image_dir = "C:/Users/Jihun/Desktop/ABC/"
target_resize_dir = "C:/Users/Jihun/Desktop/ZZZ/b/"
#target_resize_dir = "C:/Users/Jihun/Desktop/last_check/predict/"

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
    im = im.resize((800,600))
    im = im.rotate(270)
    im = im.crop((300,200,500,400))
    im = im.resize((64,64),Image.ANTIALIAS)

    '''im = im.resize((400,300))
    im = im.rotate(270)
    im = im.crop((150,100,250,200))
    size = (64,64)
    im = im.thumbnail(size)'''
    #im = im.resize((64,64))

    #im = im.filter(ImageFilter.MaxFilter) #GaussianBlur, BLUR, MaxFilter ...
    #print("i: ", count, im.format, im.size, im.mode, file.split("/")[-1])
    count+=1


    #print(target_resize_dir+file.split("\\")[-1])
    #cv2.imwrite(target_resize_dir+file.split("\\")[-1],im)
    #cv2.imwrite(os.path.join(image_dir,file.split("\\")[-1]),im)


    #im = ImageOps.fit(im, size, Image.ANTIALIAS, 0, (0.5, 0.5))
    #im.save(target_resize_dir+file.split("\\")[-1]+".jpg", quality=100)
    im.save(target_resize_dir+file.split("\\")[-1], quality=100)
