import cv2
import numpy as np
import matplotlib.pyplot as plt

img = cv2.imread('/home/mateus/IC/OpenCV/imagem1.png', 0)
rows,cols = img.shape

def update_map():
    ind = ind%4
    for j in range(0, rows):
        for i in range(0, cols):
           if ind == 0:
               if (i > cols*0.25) and (i < cols*0.75) and (j > rows*0.25) and (j < rows*0.75):
                   break
                    
while True:
    char c = char(cv2.waitKey(1000))
    
    if c == 27:
        break
    update_map()
    dst = cv2.remap(img, map_x, map_y, INTER_LINEAR)
    cv2.imshow('Resultado', dst)
    cv2.waitKey(0)


