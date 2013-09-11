# coding=utf-8
'''
Created on 2013-9-9
@author: gudh
'''

def get_color(mat, pos):
    '''获取颜色信息'''
    if pos[0] < 0 or pos[1] < 0:
        return None
    if pos[0] >= len(mat) or pos[1] >= len(mat[0]):
        return None
    return mat[pos[0]][pos[1]]

def get_avail(mat, arounds, compare_pos):
    '''获取可用的移动信息'''
    res = []
    color = get_color(mat, compare_pos)
    for roun in arounds:
        c1 = get_color(mat, roun[1])
        if (not c1 == None) and c1 == color:
            res.append(roun)
    return res

def get_middle(pos1, pos2, mat):
    '''获取两个相隔的情况下，可能的第三个的移动信息'''
    p1 = pos1
    p2 = pos2
    arounds = []
    if pos1[1] == pos2[1]: # 竖排
        # p1是小的那个
        if p1[0] > p2[0]:
            p1 = pos2
            p2 = pos1
        arounds.append(((p1[0] + 1, p1[1]), (p1[0] + 1, p1[1] - 1), ((p2[0], p1[1]), (p1[0] + 1, p1[1] - 1))))
        arounds.append(((p1[0] + 1, p1[1]), (p1[0] + 1, p1[1] + 1), ((p2[0], p1[1]), (p1[0] + 1, p1[1] + 1))))
    elif pos1[0] == pos2[0]: # 横排
        if p1[1] > p2[1]:
            p1 = pos2
            p2 = pos1
        arounds.append(((p1[0], p1[1] + 1), (p1[0] - 1, p1[1] + 1), ((p1[0], p1[1]), (p1[0], p1[1] + 1), (p2[0], p2[1]))))
        arounds.append(((p1[0], p1[1] + 1), (p1[0] + 1, p1[1] + 1), ((p1[0], p1[1]), (p1[0] + 1, p1[1] + 1), (p2[0], p2[1]))))
    
    return get_avail(mat, arounds, pos1)

def get_arround(pos1, pos2, mat):
    '''获取两个相连的情况下，可能的第三个的移动信息
返回结果为(移动位置1， 移动位置2， 受影响的行)
'''
    p1 = pos1
    p2 = pos2
    arounds = []
    size = (len(mat), len(mat[0]))
    if pos1[1] == pos2[1]: # 竖排
        # p1是小的那个
        if p1[0] > p2[0]:
            p1 = pos2
            p2 = pos1
        # 上边
        if p1[0] > 0:
            arounds.append(((p1[0] - 1, p1[1]), (p1[0] - 1, p1[1] - 1), ((p2[0], p1[1]), (p1[0] - 1, p1[1] - 1))))
            arounds.append(((p1[0] - 1, p1[1]), (p1[0] - 1, p1[1] + 1), ((p2[0], p1[1]), (p1[0] - 1, p1[1] + 1))))
            arounds.append(((p1[0] - 1, p1[1]), (p1[0] - 2, p1[1]), ((p2[0], p1[1]),)))
        # 下边
        if p2[0] < size[0] - 1:
            arounds.append(((p2[0] + 1, p2[1]), (p2[0] + 1, p2[1] - 1), ((p2[0] + 1, p1[1]), (p2[0] + 1, p1[1] - 1))))
            arounds.append(((p2[0] + 1, p2[1]), (p2[0] + 1, p2[1] + 1), ((p2[0] + 1, p1[1]), (p2[0] + 1, p1[1] + 1))))
            arounds.append(((p2[0] + 1, p2[1]), (p2[0] + 2, p2[1]), ((p2[0] + 2, p1[1]),)))
    elif pos1[0] == pos2[0]: # 横排
        if p1[1] > p2[1]:
            p1 = pos2
            p2 = pos1
        # 左边
        if p1[1] > 0:
            arounds.append(((p1[0], p1[1] - 1), (p1[0] - 1, p1[1] - 1), ((p1[0], p1[1] - 1), (p1[0], p1[1]), (p1[0], p2[1]))))
            arounds.append(((p1[0], p1[1] - 1), (p1[0] + 1, p1[1] - 1), ((p1[0] + 1, p1[1] - 1), (p1[0], p1[1]), (p1[0], p2[1]))))
            arounds.append(((p1[0], p1[1] - 1), (p1[0], p1[1] - 2), ((p1[0], p1[1] - 2), (p1[0], p1[1] - 1), (p1[0], p1[1]), (p1[0], p2[1]))))
        # 右边
        if p2[1] < size[1] - 1:
            arounds.append(((p2[0], p2[1] + 1), (p2[0] - 1, p2[1] + 1), ((p1[0], p1[1]),(p1[0], p2[1]),(p1[0], p2[1] + 1))))
            arounds.append(((p2[0], p2[1] + 1), (p2[0] + 1, p2[1] + 1), ((p1[0], p1[1]),(p1[0],  p2[1]),(p1[0] + 1,  p2[1] + 1))))
            arounds.append(((p2[0], p2[1] + 1), (p2[0], p2[1] + 2), ((p1[0], p1[1]), (p1[0], p2[1]), (p1[0], p2[1] + 1),(p1[0], p2[1] + 2))))
            
    return get_avail(mat, arounds, pos1)


def calculate_step(mat):
    '''计算下一步的所有可行情况'''
    ws = len(mat)
    hs = len(mat[0])
    
    res = []
    # 从下往上，从右往左
    for j in range(hs):
        last = None
        last2 = None
        for i in range(ws):
            if last:
                m1 = mat[last[0]][last[1]]
                m2 = mat[i][j]
                if m1 == m2:
                    # 两边
                    a = get_arround((i, j), last, mat)
                    res.extend(a)
                elif last2:
                    # 中间
                    m0 = mat[last2[0]][last2[1]]
                    if m0 == m2:
                        a = get_middle((i, j), last2, mat)
                        res.extend(a)
                last2 = last
                last = (i, j)
            else:
                last2 = last
                last = (i, j)
    # 从右往左，从下往上
    for i in range(ws):
        last = None
        last2 = None
        for j in range(hs):
            if last:
                m1 = mat[last[0]][last[1]]
                m2 = mat[i][j]
                if m1 == m2:
                    # 两边
                    a = get_arround((i, j), last, mat)
                    res.extend(a)
                elif last2:
                    # 中间
                    m0 = mat[last2[0]][last2[1]]
                    if m0 == m2:
                        a = get_middle((i, j), last2, mat)
                        res.extend(a)
                last2 = last
                last = (i, j)
            else:
                last2 = last
                last = (i, j)
    if len(res) > 20:
        print "PIC is not ok, result count is bigerr %d return no result" % len(res)
        res = []
    return res

def is_down_ok(ys, needlocs):
    '''是否在下面，如果在下面可以多个运行'''
    for loc in needlocs:
        if ys.has_key(loc[1]) and ys[loc[1]] >= loc[0]:
            return False
    return True

def add_ys(ys, needlocs):
    '''添加合并,将相同y更大的x存入'''
    for loc in needlocs:
        if ys.has_key(loc[1]):
            if ys[loc[1]] < loc[0]:
                ys[loc[1]] = loc[0]
        else:
            ys[loc[1]] = loc[0]

def get_optimal(avails):
    '''获取一张图最优的步骤集，有顺序的'''
    res = []
    ys = {}
    # 先添加同含多个的
    for ava in avails:
        if avails.count(ava) > 1:
            if not is_down_ok(ys, ava[2]):
                continue
            res.append(ava[0 : 2])
            add_ys(ys, ava[2])
    for ava in avails:
        # 如果x小于范围，则跳过
        if not is_down_ok(ys, ava[2]):
            continue
        res.append(ava[0 : 2])
        add_ys(ys, ava[2])
    return res

