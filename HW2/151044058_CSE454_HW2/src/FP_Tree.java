import java.util.ArrayList;

/*
    ALİ HAYDAR KURBAN
    151044058
    DATA MINING HOMEWORK 2
 */
public class FP_Tree
{
    /*
        These are variables to represent FP_Tree
     */
    private ArrayList<FP_Tree> childNodes = new ArrayList<>();
    private FP_Tree parentNode;
    private FP_Tree nextNode;
    private String item;
    private int counts;

    //Returns item.
    public String getItem()
    {
        return item;
    }
    //Sets item with itemValue.
    public void setItem(String itemValue)
    {
        item = itemValue;
    }
    //Returns parentNode.
    public FP_Tree getParentNode()
    {
        return parentNode;
    }
    //Sets parentNode with parentNodeValue.
    public void setParentNode(FP_Tree parentNodeValue)
    {
        parentNode = parentNodeValue;
    }
    //Returns childNodes.
    public ArrayList<FP_Tree> getChildNodes()
    {
        return childNodes;
    }
    //Returns count.
    public int getCount()
    {
        return counts;
    }
    //Sets count with countValue.
    public void setCount(int countsValue)
    {
        counts = countsValue;
    }
    //Increase count by 1
    public void increaseCount()
    {
        int tempCount = counts;
        tempCount++;
        counts = tempCount;
    }
    //Return nextNode.
    public FP_Tree getNextNode()
    {
        return nextNode;
    }
    //Sets nextNode with nextNodeValue
    public void setNextNode(FP_Tree nextNodeValue)
    {
        nextNode = nextNodeValue;
    }

}