import cv2
import numpy as np
import matplotlib.pyplot as plt

def print_result():
    plt.subplot(121),plt.imshow(img),plt.title('Input')
    plt.subplot(122),plt.imshow(dst),plt.title('Output')
    plt.show()


img = cv2.imread('/home/mateus/IC/OpenCV/imagem1.png', 0)
rows,cols = img.shape
print rows, cols
op = raw_input('S, T ou P: \n')


pts_src = np.float32([[0,0],[cols - 1,0],[0,rows - 1]])

if op == 'S' :
    x = float(raw_input('Escala em x \n'))
    y = float(raw_input('Escala em y \n'))
    pts_dst = np.float32([[0,0],[(cols - 1)*x,0],[0,y*(rows - 1)]])
    M = cv2.getAffineTransform(pts_src,pts_dst)
    dst = cv2.warpAffine(img, M, (int(cols*x),int(rows*y)))
    print_result()
    
elif op == 'T':
    pts_dst = np.float32([[0, rows*0.33], [cols*0.85, rows*0.25], [cols*0.15, rows*0.7]])
    M = cv2.getAffineTransform(pts_src, pts_dst)
    dst = cv2.warpAffine(img, M, (cols,rows))
    print_result()
    rot_mat = cv2.getRotationMatrix2D((cols/2, rows/2), -50.0, 0.6)
    dst = cv2.warpAffine(dst, rot_mat, (cols, rows))
    print_result()

elif op == 'P':
    pts_src = np.float32([[56,65],[368,52],[28,300],[389,287]])
    pts_dst = np.float32([[0,0],[500,0],[0,316],[500,316]])
    M = cv2.getPerspectiveTransform(pts_src,pts_dst)
    dst = cv2.warpPerspective(img,M,(cols,rows))
    print_result()

else:
    print 'ERRO, digite algo valido'


    
