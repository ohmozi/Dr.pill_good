# -*- coding:UTF-8 -*-

from keras.preprocessing.image import ImageDataGenerator
from keras.models import load_model
import numpy as np
from PIL import Image
#import Image
def color_predict():
    train_datagen = ImageDataGenerator(rescale=1./255)
    training_set = train_datagen.flow_from_directory('/home/ubuntu/keras_talk/train_color',   # 훈련시킬 사진 경로
                                                     shuffle=True,
                                                     seed=13,
                                                     target_size = (64, 64),    # 사이즈 지정해주기
                                                     batch_size = 15,
                                                     #color_mode='grayscale',
                                                     color_mode='rgb',
                                                     class_mode = 'categorical')
                                                     #subset="training")
    prediction_set = train_datagen.flow_from_directory('/home/ubuntu/keras_talk/test3',
                                                      target_size=(64,64),
                                                      #color_mode='grayscale',
                                                      color_mode='rgb',
                                                      batch_size=1,
                                                      class_mode =None,
                                                      shuffle=False,
                                                      seed=13)
   # print("11111")
    model = load_model('/home/ubuntu/keras_talk/cnn_attraction_keras_model_color5.h5')
    # 모델 예측하기
    prediction_set.reset()
   # print("@2222")
    output = model.predict_generator(
                prediction_set,
                steps=1,               
                verbose=1)
    #print("33333")
    predicted_class_indices = np.argmax(output,axis=1)
    labels = (training_set.class_indices)
    labels = dict((v,k) for k,v in labels.items())
    predictions = [labels[k] for k in predicted_class_indices]

    
    print(output)
    print(predictions[0])
    '''for i in range(len(predictions)):
        print(predictions[i],prediction_set.filenames[i])
        if (i+1)%4==0:
            print('\n')'''
    return predictions[0]

def shape_predict():
    train_datagen = ImageDataGenerator(rescale=1./255)
    training_set = train_datagen.flow_from_directory('/home/ubuntu/keras_talk/train_shape',   # 훈련시킬 사진 경로
                                                     shuffle=True,
                                                     seed=13,
                                                     target_size = (64, 64),    # 사이즈 지정해주기
                                                     batch_size = 15,
                                                     color_mode='grayscale',
                                                     #color_mode='rgb',
                                                     class_mode = 'categorical')
                                                     #subset="training")
    prediction_set = train_datagen.flow_from_directory('/home/ubuntu/keras_talk/test3',
                                                      target_size=(64,64),
                                                      color_mode='grayscale',
                                                      #color_mode='rgb',
                                                      batch_size=1,
                                                      class_mode =None,
                                                      shuffle=False,
                                                      seed=13)

    model = load_model('/home/ubuntu/keras_talk/cnn_attraction_keras_shape.h5')

    # 모델 예측하기

    prediction_set.reset()
    output = model.predict_generator(
                prediction_set,
                steps=1,
                verbose=1)

    predicted_class_indices = np.argmax(output,axis=1)
    labels = (training_set.class_indices)
    labels = dict((v,k) for k,v in labels.items())
    predictions = [labels[k] for k in predicted_class_indices]


    print(output)
    '''for i in range(len(predictions)):
        print(predictions[i],prediction_set.filenames[i])
        if (i+1)%4==0:
            print('\n')'''
    print(predictions[0])
    return predictions[0]
