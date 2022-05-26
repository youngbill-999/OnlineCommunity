package com.nowcoder.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
@Component
public class sensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(sensitiveFilter.class);

    //Characters for replacing illegal words
    private static final String REPLACE_CONTENT="***";

    //initialise root node
    private TrieNode rootNode  = new TrieNode();

    @PostConstruct//通过spring来进行管理，在服务器启动的时候就初始化，可以直接调用
    public void init(){
        //先要编译，编译后在target/classes下才能够找到存储敏感词汇的txt文件
        try(
                InputStream stream=this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                ){
                  String keyword;
                  while((keyword=reader.readLine())!=null){
                    this.addKeyWord(keyword);
                  }
        }catch (IOException e) {
            logger.error("failed to load sensitive words!");
        }
    }
     //
    private   void addKeyWord(String keyWord){
        TrieNode tempNode =  rootNode;
        for(int i=0;i<keyWord.length();i++)
        {
              TrieNode subNode = tempNode.getSubNode(keyWord.charAt(i));
              if(subNode==null){
                  subNode=new TrieNode();
                  tempNode.addSubNode(keyWord.charAt(i),subNode);
              }
              tempNode = subNode;

              if(i==keyWord.length()-1)
              {
                     tempNode.setKeywordEnd(true);
              }
        }
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        TrieNode tempNode = rootNode;
        int begin = 0, position = 0;
        //result
        StringBuilder sb = new StringBuilder();


        while(position<text.length()){
            char c = text.charAt(position);

            if(isSymbol(c)){
                if(tempNode==rootNode){
                    sb.append(c);begin++;
                }
                position++;
                continue;
            }
              tempNode = tempNode.getSubNode(c);
            if(tempNode==null){
                   sb.append(text.charAt(begin));
                   position = ++begin;
                   tempNode=rootNode;
            }else if(tempNode.isKeywordEnd()){
                sb.append(REPLACE_CONTENT);
                begin=++position;
                tempNode=rootNode;
            }
            else{
                  position++;
            }
        }
       if(begin<text.length())
       sb.append(text.substring(begin));
       return sb.toString();
    }
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }

    //definition of prefix tree
    private  class  TrieNode{
        private  boolean isKeywordEnd=false;//if it's the end of an illegal word?

        //child_node,simple tree with multi_child but no two
        //每一个node都只用他们的subnode这个表来存储他们的直接子节点的信息，而非全部的子树，这样做节约了空间
        private Map<Character,TrieNode> subNodes = new HashMap<>();//character is an object that wrapped a char type
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //add child
        public void addSubNode(Character c, TrieNode node){
             subNodes.put(c,node);
        }

        public  TrieNode getSubNode(Character c)
        {
            return subNodes.get(c);
        }
    }

}
