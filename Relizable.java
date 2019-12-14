
package relizable;

import java.util.Scanner;

/*
    p matrix may contains 4 values at its indexes
        *)  0       none is realizable
        *)  1       j+arr[i] is realizable
        *)  -1      j-arr[i] is realizable
        *)  2       j+arr[i] and j-arr[i] are realizable
             
            in case there is 2, it indicate the following possibilities
                *)  the realizable (T) has more than one solution 
                *)  one solution is by taking the corresponding element of array with a + sign 
                    and second is by taking that element with - sign

*/



public class Relizable {
    static int p[][];                               //  realizability matrix
    static int arrsum;                              // contains max sum of array
    static int solno = 0;                           // number of solutions
    static int T;                                   // realizable value
    static String str = "";                         // for storing all solutions
    
    public static int sum(int[] arr, int arrlen){   // sum of array
        int sum = 0;
        for (int i = 0; i < arrlen; i++) {
            sum += arr[i];
        }
        return sum;
    }
    
    public static boolean check(int[][] arr, int n, int value){     // method for checking j+arr[i] and j-arr[i] in previous row     
        if(value >= -arrsum && value <= arrsum){
            return arr[n][arrsum + value] == 1 || arr[n][arrsum + value] == 2 || arr[n][arrsum + value] == -1;
        }
        else{
            return false;
        }
    }
    
    public static boolean Relizable(int[] arr, int realizable) {
        
        p = new int[arr.length+1][2*arrsum + 1]; // realizability matrix
        
        p[0][arrsum] = 1;   // base condition
        
        for (int i = 1; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                if(check(p, i-1, j - arrsum + arr[i-1]) && check(p, i-1, j - arrsum - arr[i-1])){       // storing 2 if realizable by both j+arr[i] or j-arr[i]
                    p[i][j] = 2;
                }
                else if(check(p, i-1, j - arrsum + arr[i-1])){  // storing 1 if realizable by j+arr[i]
                    p[i][j] = 1;
                }
                else if(check(p, i-1, j - arrsum - arr[i-1])){  // storing -1 if realizable by j-arr[i]
                    p[i][j] = -1;
                }
                else{
                    p[i][j] = 0;    // default if not realizable
                }
            }
        }
        return p[arr.length][arrsum + realizable] != 0;     // realizabilility condition
    }
    
    public static String showone(int[] arr, int realizable){  
        String s = "";          
        int sum = 0;
        
        T = realizable;     
        int c = realizable; 
        int i = 0;
        
        do{
            switch (p[p.length-1-i][arrsum + c]) {
                case 1:     // if value is 1
                    c = c + arr[p.length-2 - i] * p[p.length-1 - i][arrsum + c];    //updating c in previous row
                    s = "-" + arr[p.length-2 - i] + s;                              // concatenating in s
                    sum -= arr[p.length-2 - i];                                     // updating sum
                    i++;
                    break;
                case -1:    // if value is -1
                    c = c + arr[p.length-2 - i] * p[p.length-1 - i][arrsum + c];    //updating c in previous row
                    s = "+" + arr[p.length-2 - i] + s;                              // concatenating in s
                    sum += arr[p.length-2 - i];                                     // updating sum
                    i++;
                    break;
                case 2:     // if value is 2 
                    /*
                        2 means more than one solution are possible 
                        the code will pick solution in which it finds
                            *)  minimum number of '-' sign
                            *)  maximum number of '+' sign
                    */
                    c = c - arr[p.length-2 - i] * (p[p.length-1 - i][arrsum + c] - 1);  //updating c in previous row
                    s = "+" + arr[p.length-2 - i]  + s;                                 // concatenating in s
                    sum += arr[p.length-2 - i];                                         // updating sum
                    i++;
                    break;
                default:
                    break;
            }
        }
        while(c >= -arrsum && c <= arrsum  && i < arr.length);     /*   will keep on concatenating until its all 
                                                                        array elements are written     
                                                                   */
        
        return s + " = " + sum;      
    }
    
    public static void showall(int[] arr, int realizable, int i, String s){
        if(i == -1){                                                                     // base condition
            solno++;                                                                     // incrementing solution no.
            str = str + "    Sol No. " + solno  + " : " + s + " = " + T + "\n";    // concatenating all solutions
        }
        else if(p[i+1][arrsum + realizable] == -1){                                      //if value is -1
            s = "+" + arr[i] + s;
            realizable = realizable + arr[i] * (p[i+1][arrsum + realizable]);            //updating realizable value
            showall(arr, realizable,i-1, s);                                             //recursive call
        }
        else if(p[i+1][arrsum + realizable] == 1){                                       //if value is 1
            s = "-" + arr[i] + s;
            realizable = realizable + arr[i] * (p[i+1][arrsum + realizable]);            //updating realizable value
            showall(arr, realizable,i-1, s);                                             //recursive call
        }
        else if(p[i+1][arrsum + realizable] == 2){                                       //if value is 2
            int c = realizable;                                                                 
            c = c + arr[i] * (p[i+1][arrsum + realizable] - 3); 
            showall(arr, c,i-1, "+" + arr[i] + s);                                       // recurise call for +ve solution
            
            int d = realizable;
            d = d + arr[i] * (p[i+1][arrsum + realizable] - 1);    
            showall(arr, d,i-1, "-" + arr[i] + s);                                       // recurise call for -ve solution
        }
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter array of elements:");
        String input = in.nextLine();
        System.out.println("");
        
        System.out.println("Enter Realizable Value");
        int realizable = in.nextInt();                                                           // realizable value
        System.out.println("");
        
        String[] sinput = input.split(" ");              // splitting array on space(' ')
        
        int arr[] = new int[sinput.length];              // array declaration
        
        for (int i = 0; i < arr.length; i++) {      
            boolean isDigit = true;         
            for (int j = 0; j < sinput[i].length(); j++) {
                if(!Character.isDigit(sinput[i].charAt(j))){        // character checking
                    isDigit = false;
                }
            }
            if(isDigit){
                arr[i] = Integer.parseInt(sinput[i]);       // adding element to array
            }
            else{
                System.out.println("Character Found");
                System.exit(0);                             // Program Terminates if Character Found in array
            }
        }
        
        arrsum = sum(arr, arr.length);                   // sum calculation
        
        System.out.println("n = " + arr.length);         // printing n
        System.out.println("The input Array:");
        
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");              // array element
        }
        System.out.println("");         
        System.out.println("T = " + realizable);         // printing realizable
        System.out.println("");
        
        if(-arrsum <= realizable && realizable <= arrsum){      // if realizable is out of range [-S,S]
            if(Relizable(arr, realizable)){                                 // if value is realizable
                System.out.println("Part 1: Realizability check");      
                System.out.println("    The value " + realizable + " is  realizable");  
                System.out.println("");
                System.out.println("Part 2: One solution");
                System.out.println("    Solution:   " + showone(arr, realizable));  //print one solution
                System.out.println("");
                System.out.println("Part 3: All solutions");
                showall(arr, realizable, arr.length-1, "");                         //store all solution in str
                System.out.println("    No of Solutions = " + solno);
                System.out.println(str);
            }
            else{       // if not realizable    
                System.out.println("\nPart 1: Realizability check\n    The value " + realizable + " is not realizable\nPart 2: One solution\n    The value " +  realizable + " is not realizable\nPart 3: All solutions\n    Number of solutions = 0");
            }
        }
        else{       // if out of range
            System.out.println(realizable + " does not lie in the range [-" + arrsum + " to " + arrsum + "]\nWhere,\n    " + arrsum + " is the total sum of all elements of array");
        }   
    } 
}
