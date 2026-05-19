package com.jeesite.modules.test;

import java.util.*;

import static com.jeesite.common.utils.ThreadUtils.sleep;

public class CalculateUtil {

    public static void main(String[] args) {
        /**
         * 1. 两数之和（最常考）
         */
        /*int[] nums = new int[]{2, 7, 11, 15};
        int[] twoSum = twoSum(nums, 13);
        System.out.println(nums[twoSum[1]]);*/
        /*
        * 2、 找出数组中重复数字
        * */
        /*int[] nums2 = new int[]{1, 3, 4, 2, 4};
        int findDuplicate = findDuplicate(nums2);
        System.out.println(findDuplicate);*/
        /*
         * 3. 数组中出现次数超过一半的数字
         * */
        /*int[] nums3 = new int[]{2, 2, 3, 2, 2, 2, 2, 4, 2,1,5,1};
        int majorityElement = majorityElement(nums3);
        System.out.println(majorityElement);*/
        /**
         * 4. 连续子数组最大和（最大子序和）
         *
         */
        /*int[] nums4 = new int[]{-2,1,-3,4,-1,2,1,-5,4};
        int maxSubArray = maxSubArray(nums4);
        System.out.println(maxSubArray);*/
        /**
         * 8. 快速排序
         */
        int[] nums8 = new int[]{3, 2, 1, 10, 9, 6, 7, 8, 4, 5};
        int[] quickSort = quickSort(nums8);
        System.out.println(Arrays.toString(quickSort));

    }
    /**
     * 1. 两数之和（最常考）
     * 题目：给定数组和目标值，找两个数下标，和为 target
     * 用map存储数组的值和下标，然后遍历数组，计算target-nums[i]，如果map中存在，则返回下标，如果不存在，则将nums[i]和i存入map中
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        return null;


    }
    /**
     * 2、 找出数组中重复数字
     */
    public static int findDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) {
                return num;
            }
        }
        return -1;
    }
    /**
     * 3. 数组中出现次数超过一半的数字
     * new int[]{1, 2, 3, 2, 2, 2, 2, 4, 2,1,5}
     */
    public static int majorityElement(int[] nums) {
        int count = 0;
        int candidate = 0;
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            System.out.print("num="+num+"candidate="+candidate+",");
            if (num == candidate) {
                count++;
            } else {
                count--;
            }
            System.out.println("count="+count);
        }
        return candidate;
    }
    /*
    * 4. 连续子数组最大和（最大子序和）
    * nums = [-2,1,-3,4,-1,2,1,-5,4]，正确答案是 6
    * */
    public static int maxSubArray(int[] nums){
        int max = nums[0];
        int sum = 0;
        for (int num : nums) {
            if (sum > 0) {
                sum += num;
            } else {
                sum = num;
            }
            max = Math.max(max, sum);
            System.out.println("num1="+num+",max="+max+",sum="+sum);
        }
        return max;
    }
    /**
     * 5.冒泡排序
     */
    public static int[] bubbleSort(int[] nums){
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
        return nums;
    }
    /**
     * 6. 选择排序
     */
    public static int[] selectionSort(int[] nums){
        for (int i = 0; i < nums.length; i++) {
            int min = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[min]) {
                    min = j;
                }
            }
            int temp = nums[i];
            nums[i] = nums[min];
            nums[min] = temp;
        }
        return nums;
    }
    /**
     * 7.二分查找
     */
    public static int binarySearch(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
    /**
     * 8. 快速排序
     */
    public static int[] quickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
        return nums;
    }
    private static void quickSort(int[] nums, int low, int high) {
        if (low >= high) {
            return;
        }
        int pivot = partition(nums, low, high);
        quickSort(nums, low, pivot - 1);
        quickSort(nums, pivot + 1, high);
    }
    private static int partition(int[] nums, int low, int high) {
        int pivot = nums[high];
        int i = low;
        for (int j = low; j < high; j++) {
            if (nums[j] < pivot) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
                i++;
            }
        }
        System.out.println(Arrays.toString(nums));
        // 将基准放到正确的位置（i 是基准的最终索引）
        int temp = nums[i];
        nums[i] = nums[high];
        nums[high] = temp;
        System.out.println(Arrays.toString(nums));

        return i; // 返回基准的最终位置
    }



}
