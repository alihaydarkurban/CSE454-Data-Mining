import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
    ALİ HAYDAR KURBAN
    151044058
    DATA MINING HOMEWORK 2
 */
public class FP_Growth
{
    /*
        This method reads the file.
        Create a 2D ArrayList to use file inputs
        2D ArrayList includes all file without (, . ") and ect.
    */
    private static ArrayList<ArrayList<String>> createDataset(String filename)
    {
        ArrayList<ArrayList<String>> myArrayList = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filename)))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                ArrayList<String> items = new ArrayList<>();
                String[] arrayOfString = line.split("[, ?.@]+");
                for(int i = 0; i < arrayOfString.length; ++i)
                {
                    items.add(arrayOfString[i]);
                }
                myArrayList.add(items);
            }
        } catch (IOException e)
        {
            System.err.format("IOException: %s%n", e);
        }
        return myArrayList;
    }

    /*
        This method prints the 2D ArrayList.
        This 2D ArrayList represents my data set.
     */
    private static void printDataset(ArrayList<ArrayList<String>> itemSet)
    {
        for(int i = 0; i < itemSet.size(); ++i)
        {
            ArrayList<String> lineOfDS = itemSet.get(i);
            for(int j = 0; j < lineOfDS.size(); ++j)
            {
                System.out.print(lineOfDS.get(j) + " ");
            }
            System.out.println();
        }
    }

    /*
        This method takes a 2D ArrayList which represents the data set.
        And finds all items and their amounts.
        Creates a HashMap with key Item Name, with value Item Amounts.
        Returns the HaspMap with names and amounts.
     */
    private static HashMap<String,Integer> findItemsAndCounts(ArrayList<ArrayList<String>> itemSet)
    {
        int initialCount = 1;
        HashMap<String,Integer> countOfItems = new HashMap<>();
        for(int i = 0; i < itemSet.size(); ++i)
        {
            ArrayList<String> lineOfDS = itemSet.get(i);
            for(int j = 0; j < lineOfDS.size(); ++j)
            {
                if(countOfItems.containsKey(lineOfDS.get(j)))
                {
                    int tempCount = countOfItems.get(lineOfDS.get(j));
                    tempCount++;
                    countOfItems.replace(lineOfDS.get(j),tempCount);
                }
                else
                {
                    countOfItems.put(lineOfDS.get(j),initialCount);
                }
            }
        }
        return countOfItems;
    }

    /*
        This method prints the HashMap.
        This HashMap represents items and their counts.
    */
    private static void printHashMap(HashMap<String, Integer> hashMap)
    {
        for (String key : hashMap.keySet())
        {
            System.out.print(key + " : ");
            System.out.println(hashMap.get(key));
        }
    }

    /*
        This method takes a HashMap and sort it.
        Sorting depends on value which represents the amount of item.
        Sorting is reverse order from largest to smallest.
     */
    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hashMap)
    {
        ArrayList<Integer> Values = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        for (String key : hashMap.keySet())
        {
            Values.add(hashMap.get(key));
        }

        Collections.sort(Values, Collections.reverseOrder());

        for (int value : Values)
        {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet())
            {
                if (entry.getValue().equals(value))
                {
                    sortedMap.put(entry.getKey(), value);
                }
            }
        }
        return sortedMap;
    }

    /*
        This method takes a HashMap and an integer min_sup.
        Removes items which amount is less then min_sup.
        Sorts and returns the HashMap.
    */
    private static HashMap<String,Integer> removeItemsLessThanMinSup(HashMap<String, Integer> hashMap, int min_sup)
    {
        HashMap<String,Integer> returnHashMap = new HashMap<>();
        for (String key : hashMap.keySet())
        {
            int value = hashMap.get(key);

            if(value >= min_sup)
            {
                returnHashMap.put(key,value);
            }
        }
        return sortByValue(returnHashMap);
    }

    /*
        This method takes two 2D ArrayList.
        First represents data set, second represent frequent item set.
        This method creates heads for itemSet and creates tree.
        This method also calls a print method to print frequents pattern.
     */
    public void FPGrowthMethod(ArrayList<ArrayList<String>> itemSet, ArrayList<String> frequentItemSets)
    {
        ArrayList<FP_Tree> heads = buildHeadTable(itemSet); //Build heads of items

        FP_Tree root = buildFPTree(itemSet, heads); //Build FP_Tree

        //Best-case of recursive method
        if(root.getChildNodes().size() == 0)
        {
            return;
        }

        printPattern(frequentItemSets, heads); //Prints pattern

        //This is for finding pattern
        for(int i = 0; i < heads.size(); ++i)
        {
            ArrayList<String> pattern = new ArrayList<>();

            String itemOfHead = heads.get(i).getItem();
            pattern.add(itemOfHead);

            if(frequentItemSets != null)
            {
                pattern.addAll(frequentItemSets);
            }

            ArrayList<ArrayList<String>> newItemSet = new ArrayList<>();
            FP_Tree currentNode = heads.get(i).getNextNode();

            while(currentNode != null)
            {
                int counter = currentNode.getCount();
                ArrayList<String> parentNodes = new ArrayList<>();

                FP_Tree parent = currentNode; //parent is our currentNode

                //Traverse all parent nodes of currentNode and put them into parentNodes
                while((parent = parent.getParentNode()).getItem() != null)
                {
                    String item = parent.getItem();
                    parentNodes.add(item);
                }

                while(counter > 0)
                {
                    newItemSet.add(parentNodes);
                    counter--;
                }

                currentNode = currentNode.getNextNode();
            }

            //Recursive call
            FPGrowthMethod(newItemSet, pattern);
        }
    }

    /*
        This method takes an ArrayList with String and an ArrayList with FP_Tree
        This method prints the frequent pattern.
     */
    private void printPattern(ArrayList<String> frequentItemSets, ArrayList<FP_Tree> heads)
    {
        if(frequentItemSets != null)
        {
            for(int i = 0; i < heads.size(); ++i)
            {
                System.out.print("{ ");
                for(int j = 0; j < frequentItemSets.size(); ++j)
                {
                    System.out.print(frequentItemSets.get(j) + " ");
                }
                System.out.println(heads.get(i).getItem() + " : " + heads.get(i).getCount() + " }");
            }
        }
    }

    /*
        This method takes a 2D ArrayList.
        Creates HashMap with this 2D ArrayList.
        It uses findItemsAndCounts and removeItemsLessThanMinSup methods.
        It create a ArrayList with FP_Tree and adds key of HashMap.
     */
    private ArrayList<FP_Tree> buildHeadTable(ArrayList<ArrayList<String>> itemSet)
    {
        ArrayList<FP_Tree> head = new ArrayList<>();
        HashMap<String, Integer> itemMap;

        itemMap = findItemsAndCounts(itemSet);
        itemMap = removeItemsLessThanMinSup(itemMap, MIN_SUPPORT); //It was sorted inside of this method.

        for (String key : itemMap.keySet())
        {
            FP_Tree FP_Tree_Node = new FP_Tree();
            FP_Tree_Node.increaseCount();
            FP_Tree_Node.setItem(key);
            FP_Tree_Node.setCount(itemMap.get(key));
            head.add(FP_Tree_Node);
        }
        return head;
    }

    /*
        This method takes a 2D ArrayList and an ArrayList with FP_Tree.
        This method create a FP_Tree and returns it.
        It uses findChildNodes and addNode methods to create FP_Tree.
     */
    private FP_Tree buildFPTree(ArrayList<ArrayList<String>> itemSet, ArrayList<FP_Tree> heads)
    {
        FP_Tree root = new FP_Tree();
        FP_Tree currentNode = root;

        for(ArrayList<String> items : itemSet)
        {
            for(String item : items)
            {
                FP_Tree tempTree = findChildNodes(currentNode,item);

                if(null == tempTree)
                {
                    tempTree = new FP_Tree();
                    tempTree.setItem(item);
                    tempTree.setParentNode(currentNode);
                    currentNode.getChildNodes().add(tempTree);
                    addNode(heads,tempTree);
                }
                currentNode = tempTree;
                tempTree.increaseCount();
            }
            currentNode = root;
        }
        return root;
    }

    /*
        This method takes a FP_Tree and String.
        It returns a FP_Tree which its item and String item are equal.
     */
    private FP_Tree findChildNodes(FP_Tree currentNode, String item)
    {
        ArrayList<FP_Tree> children = currentNode.getChildNodes();
        if(children != null)
        {
            for(int i = 0; i < children.size(); ++i)
            {
                String currentItem = children.get(i).getItem();
                if(currentItem.equals(item))
                {
                    return children.get(i);
                }
            }
        }
        return null;
    }

    /*
        This method takes an ArrayList with FP_Tree and a FP_Tree
        This method links the nodes with the same item to the heads
     */
    private void addNode(ArrayList<FP_Tree> heads, FP_Tree FP_Tree_Node)
    {
        FP_Tree currentNode;

        for(int i = 0; i < heads.size(); ++i)
        {
            String headItem = heads.get(i).getItem();
            String nodeItem = FP_Tree_Node.getItem();

            if(headItem.equals(nodeItem))
            {
                currentNode = heads.get(i);

                while(currentNode.getNextNode() != null)
                {
                    currentNode = currentNode.getNextNode();
                }
                currentNode.setNextNode(FP_Tree_Node);
            }
        }
    }

    /*
        This is a method to separate outputs.
     */
    private static void printSeparator()
    {
        System.out.println("==========================================================");
    }

    /*
        This is test cases of my implementation.
     */
    private static void TestDataset()
    {
        ArrayList<ArrayList<String>> Dataset;

        printSeparator();
        Dataset = createDataset(FILE_NAME);
        System.out.println("Dataset");
        printDataset(Dataset);
        printSeparator();

        printSeparator();
        System.out.println("Items and Counts");
        var ItemsAndCounts = findItemsAndCounts(Dataset);
        printHashMap(ItemsAndCounts);
        printSeparator();

        printSeparator();
        System.out.println("Ordered Items and Counts");
        var sortedItem = sortByValue(ItemsAndCounts);
        printHashMap(sortedItem);
        printSeparator();

        printSeparator();
        System.out.println("After Removing Items Less Than Minimum Support(" + MIN_SUPPORT + ")");
        var a = removeItemsLessThanMinSup(sortedItem, MIN_SUPPORT);
        printHashMap(a);
        printSeparator();

        FP_Growth fpg = new FP_Growth();
        System.out.println("Frequent Patterns");
        fpg.FPGrowthMethod(Dataset,null);
        printSeparator();

    }

    private static int MIN_SUPPORT;
    private static String FILE_NAME;
    public static void main(String [] args)
    {
        //Test Dataset1.txt
        printSeparator();
        System.out.println("*******************TEST OF Dataset1.txt******************");
        MIN_SUPPORT = 3;
        FILE_NAME = "Dataset1.txt";
        TestDataset();

        //Test Dataset2.txt
        System.out.println("*******************TEST OF Dataset2.txt******************");
        MIN_SUPPORT = 2;
        FILE_NAME = "Dataset2.txt";
        TestDataset();

    }
}