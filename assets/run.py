# coding=utf-8
'''
Created on 2013-9-9

@author: gudh
'''
import time
import xiaochu, xcalg

def get_step(path):
    img = Image.open(path)
    start = time.clock()
    mat = xiaochu.get_pic_info(img)
    if not mat:
        return None
    res = xcalg.calculate_step(mat)
    for r in res:
        print r
    res = xcalg.get_optimal(res)
    print "------"
    pos = []
    for r in res:
        print r
        # 坐标和序号是反的
        m = xiaochu.get_rc_pos(r[0])
        n = xiaochu.get_rc_pos(r[1])
        pos.append((m, n))
        print (m, n)
    end = time.clock()
    print "GetStep Time: ", end - start
    return pos

if __name__ == '__main__':
    import Image
    for i in range(17, 18):
        print i
        print
        get_step(r"D:/m.png")
        print
        print "-" * 20
