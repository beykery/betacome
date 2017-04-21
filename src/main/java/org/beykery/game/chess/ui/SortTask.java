package org.beykery.game.chess.ui;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

class SortTask extends RecursiveAction
{

  final int[] array;
  final int lo;
  final int hi;
  private int THRESHOLD = 200; //For demo only  

  public SortTask(int[] array)
  {
    this.array = array;
    this.lo = 0;
    this.hi = array.length - 1;
  }

  public SortTask(int[] array, int lo, int hi)
  {
    this.array = array;
    this.lo = lo;
    this.hi = hi;
  }

  @Override
  protected void compute()
  {
    if (hi - lo < THRESHOLD)
    {
      sequentiallySort(array, lo, hi);
      //  System.out.println("array,NO \n" + Arrays.toString(array));
    } else
    {
      int pivot = partition(array, lo, hi);
      //System.out.println("\npivot = " + pivot + ", low = " + lo + ", high = " + hi);
      //System.out.println("array" + Arrays.toString(array));
      invokeAll(new SortTask(array, lo, pivot - 1), new SortTask(array, pivot + 1, hi));
    }
  }

  private int partition(int[] array, int lo, int hi)
  {
    long x = array[hi];
    int i = lo - 1;
    for (int j = lo; j < hi; j++)
    {
      if (array[j] <= x)
      {
        i++;
        swap(array, i, j);
      }
    }
    swap(array, i + 1, hi);
    return i + 1;
  }

  private void swap(int[] array, int i, int j)
  {
    if (i != j)
    {
      int temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }
  }

  private void sequentiallySort(int[] array, int lo, int hi)
  {
    Arrays.sort(array, lo, hi + 1);//Only one question!  
  }

  public static void main(String... args) throws InterruptedException
  {
    int[] array = new int[150000000];
    Random rand = new Random();
    for (int i = 0; i < array.length; i++)
    {
      array[i] = rand.nextInt(); //For demo only  
    }
    System.out.println("start:");
    long s = System.currentTimeMillis();

//        ForkJoinTask sort = new SortTask(array);
//        ForkJoinPool fjpool = new ForkJoinPool();
//        fjpool.execute(sort);
//        fjpool.shutdown();
//        boolean r = fjpool.awaitTermination(30, TimeUnit.SECONDS);
//        System.out.println(r);
    Arrays.sort(array);
    System.out.println(System.currentTimeMillis() - s);
  }
}
