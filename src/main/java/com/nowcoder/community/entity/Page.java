package com.nowcoder.community.entity;

/**
 * 封装分页相关的信息.
 */
public class Page {
    //当前页码
    private  int current=1;

    //显示上限
    private  int limit=10;

    //数据总数
    private int rows;

    //查询路径
    private String path;

    public int getCurrent() {

        return current;
    }

    public void setCurrent(int current) {
        //需要添加一些数据限定
        if(current>=1)
        { this.current = current;}

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=1 && limit<=100)
        {this.limit = limit;}
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows>=0)
        {this.rows = rows;}
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOffset(){
        //通过当前页的页码查询需要查询的起始行
        return (current-1)*limit;
    }

    public int getTotal(){
        //获取总的页数
        if(rows%limit==0)
        {
            return rows/limit;
        }
        else
        {
            return rows/limit+1;
        }
    }


    //根据起始页来计算当前页码栏里显示的页的范围，因为不可能所有的页都显示出来
    public int getFrom(){
           int from = current-2;
           return from<1?1:from;
    }

    public int getTo(){
          int to=current+2;
          int total= getTotal();
          return to>total?total:to;
    }


}
