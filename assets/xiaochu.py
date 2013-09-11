# encoding=utf-8
'''
Created on 2013-9-8

@author: gudh
'''

start_pos = (5, 222) # 开始的位置
block_size = (67, 67) # 块大小
rel_pos = (33, 28) # 相对块头位置
colors = (
(255, 255, 255), # 白
(164, 130, 213), # 紫
(247, 214, 82), # 黄
(244, 160, 90), # 土
(90, 186, 238), # 蓝
(247, 69, 95), # 红
(173, 235, 82) # 绿
)
colornames = (u'ba', u'zh', u'hu', u'tu', u'la', u'ho', u'lv')
ax = (35, 35, 35) # 允许的误差


def get_pix(img):
    '''获取测试开始位置，块大小'''
    m = 5
    n = 222 + 67
    x = 67
    for i in range(7):
        print "c%d = %s" % (i + 1, get_color(img, m + i * x + 33, n + 20)[0:3])

def get_pos(i, j):
    '''获取块内判断的点'''
    x = start_pos[0] + i * block_size[0] + rel_pos[0]
    y = start_pos[1] + j * block_size[1] + rel_pos[1]
    return (x, y)

def get_rc_pos(rc):
    '''获取rc的点，注意横纵是反的'''
    x = start_pos[0] + rc[1] * block_size[0] + rel_pos[0]
    y = start_pos[1] + rc[0] * block_size[1] + rel_pos[1]
    return (x, y)

def get_block(i, j):
    '''获取块的区域'''
    x = start_pos[0] + i * block_size[0]
    y = start_pos[1] + j * block_size[1]
    w = x + block_size[0]
    h = y + block_size[1]
    return (x, y, w, h)

def similar_color(p, color):
    '''判断是否是相似'''
    #print p, color
    for i in range(3):
        if abs(p[i] - color[i]) >= ax [i]:
            return False
    return True

def get_color(img, i, j):
    '''获取像素点的颜色'''
    p = get_pos(i, j)
    #print p
    index = 0
    #color = img.getRawPixel(p[0], p[1])[1:]
    color = img.getpixel(p)
    for index in range(len(colors)):
        if similar_color(color, colors[index]):
            return index
    return -1

def get_pic_info(img):
    '''获取像素矩阵'''
    mat = []
    blank_c = 0
    for j in range(7):
        mx = [] 
        for i in range(7):
            c = get_color(img, i, j)
            mx.append(c)
            if c == -1:
                blank_c += 1
        mat.append(mx)
    print_mat(mat)
    if(blank_c > 7):
        print "blank is %d, return None" % blank_c
        mat = None
    return mat

def cut_all(img):
    '''将所有单独的块截图保存'''
    for j in range(7):
        for i in range(7):
            b = get_block(i, j)
            im = img.crop(b)
            im.save("c:/m/%d%d.jpg" % (i, j), "JPEG")

def print_mat(mat):
    '''输出结果矩阵'''
    print ".", "|", "0   1   2   3   4   5   6"
    i = 0
    for m in mat:
        print i,"|",
        i += 1
        for n in m:
            if n < 0:
                print "No",
            else:
                print colornames[n], 
        print 

if __name__ == "main":
    import Image
    img = Image.open(r"c:/m.png")
    mat = get_pic_info(img)

