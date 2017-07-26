import cv2
import numpy as np
import matplotlib.pyplot as plt

img = cv2.imread('/home/mateus/IC/OpenCV/imagem1.png', 0)


op = raw_input('S para escala, T para translacao ou R para rotacao \n')
print op

if op == 'S':
    x = float(raw_input('Escala em x \n'))
    y = float(raw_input('Escala em y \n'))
    res = cv2.resize(img, None, fx=x, fy=y, interpolation=cv2.INTER_CUBIC)
    cv2.imshow('Input', img)
    cv2.waitKey(0)
    cv2.imshow('Output', res)
    cv2.waimgitKey(0) 

elif op == 'T': 
    x = float(raw_input('Deslocamento em x \n'))
    y = float(raw_input('Deslocamento em y \n'))
    linhas, colunas = img.shape
    Mat = np.float32([[1,0,x],[0,1,y]])
    res = cv2.warpAffine(img, Mat, (colunas, linhas)) 
    plt.subplot(121),plt.imshow(img),plt.title('Input')
    plt.subplot(122),plt.imshow(res),plt.title('Output')
    plt.show()
    
elif op == 'R':
    linhas, colunas = img.shape
    angulo = float(raw_input('Angulo de rotacao \n'))
    Mat = cv2.getRotationMatrix2D((colunas/2, linhas/2), 90, 1)
    res = cv2.warpAffine(img, Mat, (colunas, linhas)) 
    plt.subplot(121),plt.imshow(img),plt.title('Input')
    plt.subplot(122),plt.imshow(res),plt.title('Output')
    plt.show()
