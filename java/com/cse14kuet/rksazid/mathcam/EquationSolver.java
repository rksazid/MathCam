package com.cse14kuet.rksazid.mathcam;

/**
 * Created by rksazid on 12/5/2017.
 */

import android.util.Log;

import java.util.Vector;

class EquationSolver {

    private String Str;
    private String result="";


    EquationSolver(String s) {
        Str = s;
        try {
            process();
        }catch (Exception e){
            Log.e( "RESULT", " "+ e);
        }
    }

    private void process(){
        String  Str1 = ""  , Str2="";
        char flag = ' ';

        boolean isSecondString = false ;
        for (int i = 0; i < Str.length() ; i++) {
            if(Str.charAt(i)==' ') {
                //nothig to do
            }
            else if(Str.charAt(i)=='\n'|| isSecondString) {
                if(isSecondString){
                    if (Str.charAt(i)=='o'||Str.charAt(i)=='O'){
                        Str2 += '0';
                    }else{
                        Str2 += Str.charAt(i);
                    }
                }else {
                    isSecondString = true;
                }

            }
            else if (Str.charAt(i)=='o'||Str.charAt(i)=='O') {
                Str1 += '0' ;
            }
            else {
                Str1 += Str.charAt(i);
            }
        }
        /*
        Log.e( "RESULT", Str1 );
        result+=Str1;
        result+='\n';
        */

        for(int i=0; i<Str1.length(); i++)
        {
            if(Str1.charAt(i)== 'y' && Str1.charAt(i+1) == '2')
            {
                flag = 'c' ;
                break ;
            }

            else if( (Str1.charAt(i)>=65 && Str1.charAt(i+1) == '3') || (Str1.charAt(i)>= 65 && Str1.charAt(i+1)== '2' ) )
            {
                flag ='p';
            }

            else if (Str1.charAt(i)>=65)
            {
                if( flag!='p' && flag != 'c')
                    flag = 's' ;
            }

        }
        //comment
        boolean SYSTEM_OF_EQUATION = false,X=false,Y=false;
        if(flag == 's'){
            for(int i=0;i<Str1.length();i++){
                if(Str1.charAt(i) == 'x' || Str1.charAt(i) == 'X'){
                    X=true;
                }else if(Str1.charAt(i) == 'y' || Str1.charAt(i) == 'Y'){
                    Y=true;
                }
            }
        }
        if(X && Y){
            SYSTEM_OF_EQUATION = true;
        }

        if(flag =='c')
        {
            Log.e( "RESULT", "It is a Circle!!" );
            result+="It is a Circle!!";
            result+='\n';
            CircleEquation(Str1);
        }
        else if(flag=='p')
        {
            Log.e( "RESULT", "It is a polynomial Equation!! ");
            result+="It is a polynomial Equation!! ";
            result+='\n';

            coefficientDitctForPolynomial( Str1);
        }
        else if(SYSTEM_OF_EQUATION)
        {

            int[] FirstString ;
            int[]  secondString ;

            FirstString = StraitLineEquation(Str1) ;
            int a1, b1 , c1  ;
            a1 = FirstString[0];
            b1 = FirstString[1] ;
            c1 = FirstString[2] ;

            if(isSecondString){
                secondString = StraitLineEquation(Str2) ;
                int a2, b2, c2 ;
                a2 = secondString[0];
                b2 = secondString[1] ;
                c2 = secondString[2] ;
                StratLineSolved(2 ,a1, b1 , c1,a2, b2, c2) ;
                Log.e( "RESULT", "System of Equation!!");
                result+="System of Equation!!";
                result+='\n';
            }else {
                StratLineSolved(1 ,a1,b1,c1);
                Log.e( "RESULT", "It is a Straight line!!");
                result+="It is a Straight line!!";
                result+='\n';
            }

        }else{
            result = "Invalid equation";
        }
    }

    private	void CircleEquation(String string)   // it
    {
        int num =0, coeff =0, m =0  ;

        String str = "" ;

        for (int i = 0; i < string.length() ; i++) {
            if(string.charAt(i)==' ')
                continue ;
            str += string.charAt(i);
        }

        for(int i=0 ; i< str.length(); i++)
        {
            if(str.charAt(i)>='0' && str.charAt(i)<='9')
            {
                num *= 10;
                num += str.charAt(i) - '0' ;
                m = num ;
            }
            else num=0 ;

            if(str.charAt(i)>= 'y' && m!=0)
            {
                coeff = m ;
            }

            if(i>=1)
                if(str.charAt(i-1)=='+' && str.charAt(i)=='y')
                    coeff = 1 ;
        }

        double ra = num / coeff ;
        double radius = Math.sqrt(ra) ;
        Log.e( "RESULT", "Center (0,0) \n Radius = " +  String.format("%.2f",radius));

        result+="Center (0,0) \n Radius  " +  String.format("%.2f",radius);
        result+='\n';

    }


    private void coefficientDitctForPolynomial(String str)
    {
        int m2=0,m3=0,m4=0;
        int num =0 ;
        boolean flag = false, flag1= false  ;
        int[] coefficientArray1 = new int[50] ;
        String string = "" ;

        for(int i=0;i<str.length();i++){
            coefficientArray1[i]= 1;
        }
        coefficientArray1[0]= 0;
        int qq = 0 ;

        for (int i = 0; i < str.length() ; i++) {
            if(str.charAt(i)==' ')
                continue ;
            string += str.charAt(i);
        }

        for(int i=0 ; i<string.length()-1;i++ ){

            if(flag1){

                if(string.charAt(i)>= '0' && string.charAt(i)<= '9' ){
                    num*=10;
                    num += string.charAt(i)- '0' ;
                    qq = num ;

                    if(i>1)
                        if ( string.charAt(i-1)== '+' || string.charAt(i-1)=='=')
                        {
                            qq = - num ;

                            flag=true ;
                        }
                    if(flag){
                        qq = - num ;
                    }
                }

                else{
                    num=0;
                }
                coefficientArray1[0] = qq ;
            }
            else
            {

                if(string.charAt(i)>= '0' && string.charAt(i)<= '9' ){
                    num*=10;
                    num += string.charAt(i)- '0' ;
                    qq = num ;


                    if(i>1)
                        if ( string.charAt(i-1)== '-')
                        {
                            qq = - num ;
                            flag=true ;
                        }
                    if(flag){
                        qq = - num ;

                    }
                }

                else{
                    num=0;
                }

                if(string.charAt(i)>= 65 && string.charAt(i+1) == '2' ){

                    if (qq!=0) {
                        coefficientArray1[2]= qq;
                        flag= false;
                    }
                    if(i>=1){
                        if ( string.charAt(i-1)== '-')
                        {
                            m3=-1 ;
                            coefficientArray1[2] =-1;
                        }
                        else m3=1;
                    } else m3= 1 ;
                }

                if(string.charAt(i)>=65 && string.charAt(i+1) == '3'){
                    if (qq!=0) {
                        coefficientArray1[3]= qq;
                        flag = false ;
                    }
                    if(i>=1){
                        if ( string.charAt(i-1)== '-')
                        {
                            m4=-1 ;
                            coefficientArray1[3] =-1;
                        }
                        else m4=1;
                    } else m4=1 ;

                }

                if(string.charAt(i)>=65 &&  (string.charAt(i+1)== '+' || string.charAt(i+1)=='-' || string.charAt(i+1) == '=')){
                    if (qq!=0) {
                        coefficientArray1[1]= qq ;
                        flag = false ;
                    }
                    if(i>=1){
                        if ( string.charAt(i-1)== '-')
                        {
                            m2=-1 ;
                            coefficientArray1[1] =-1;
                        }
                        else m2=1; }
                    else m2=1;
                }

                if(string.charAt(i) == '+' || string.charAt(i)== '-' || string.charAt(i) == '=' ){

                    coefficientArray1[0] = qq ;
                    qq = 0 ;

                }

                if((qq == 2 || qq == 3) && (string.charAt(i+1)== '+' || string.charAt(i+1)== '-') )
                    qq=0 ;

                if(string.charAt(i)== '=')
                    flag1 = true ;
            }

        }


        if(m4==0) coefficientArray1[3]=0 ;
        if(m3==0) coefficientArray1[2]=0 ;
        if(m2==0) coefficientArray1[1]=0 ;
        int a1 = 0, b1=0, c1=0,d1=0;

        if(coefficientArray1[3]==0)
        {


            a1 = coefficientArray1[2];
            b1 = coefficientArray1[1];
            c1 = coefficientArray1[0];
            // System.out.println("a=" + a1 + "b = " + b1 + "c = " + c1 );

            BionomialEquation(a1,b1,c1) ;
        }
        else
        {
            for(int i=0; i<4; i++)
            {
                a1 = coefficientArray1[3];
                b1 = coefficientArray1[2];
                c1 = coefficientArray1[1];
                d1 = coefficientArray1[0];
                //  cout<<coefficientArray1[i] << " " ;

            }
            CubicEquation( a1, b1,c1, d1) ;
            //cout<< a1 << " " << b1 << " " << c1 << " " << d1 ;
        }

    }


    private void  BionomialEquation(int a, int b, int c)
    {

        float x1, x2, discriminant, realPart, imaginaryPart;


        discriminant = b*b - 4*a*c;

        if (discriminant > 0) {
            x1 = (float) ((-b +Math.sqrt(discriminant)) / (2*a));
            x2 = (float) ((-b - Math.sqrt(discriminant)) / (2*a));
            Log.e( "RESULT", "Roots are real and different." );

            result+="Roots are real and different.";
            result+='\n';

            Log.e( "RESULT", "x1 = "+ String.format("%.2f",x1));
            result+="x1 = "+ String.format("%.2f",x1);
            result+='\n';

            Log.e( "RESULT", "x2 = "+ String.format("%.2f",x2));
            result+= "x2 = "+ String.format("%.2f",x2);
            result+='\n';

        }

        else if (discriminant == 0) {
            Log.e( "RESULT", "Roots are real and same.");
            result+= "Roots are real and same.";
            result+='\n';



            x1 = (float) ((-b + Math.sqrt(discriminant)) / (2*a));
            Log.e( "RESULT", "x1 = x2 = " + String.format("%.2f",x1) );
            result+= "x1 = " + String.format("%.2f",x1);
            result+='\n';
            result+= "x2 = " + String.format("%.2f",x1);
            result+='\n';


        }

        else {
            realPart = -b/(2*a);
            imaginaryPart =(float) (Math.sqrt(-discriminant)/(2*a));
            Log.e( "RESULT", "Roots are complex and different.");
            result+= "Roots are complex and different.";
            result+='\n';

            Log.e( "RESULT", "x1 = " + String.format("%.2f",realPart) + "+" + String.format("%.2f",imaginaryPart) + "i");
            result+= "x1 = " + String.format("%.2f",realPart) + "+" + String.format("%.2f",imaginaryPart) + "i";
            result+='\n';

            Log.e( "RESULT", "x2 = " + String.format("%.2f",realPart) + "-" + String.format("%.2f",imaginaryPart) + "i");
            result+= "x2 = " + String.format("%.2f",realPart) + "-" + String.format("%.2f",imaginaryPart) + "i";
            result+='\n';

        }


    }

    private void CubicEquation(double a1 , double b1 , double c1, double d1 )
    {


        double f, g, h;
        double i, j, k, l, m, n, p;
        double r, s, t, u;
        double x1, x2, x2re, x2im, x3re, x3im, x3;

        f = ((3*c1/a1)-((b1*b1)/(a1*a1)))/3;
        g = ((2*(b1*b1*b1)/(a1*a1*a1))-(9*b1*c1/(a1*a1))+(27*d1/a1))/27;
        h = ((g*g)/4)+((f*f*f)/27);

        if(f==0 && g==0 && h==0){     // all roots are real and equal
            x1 = Math.pow((d1/a1),0.3333333);
            x2 = Math.pow((d1/a1),0.3333333);
            x3 = Math.pow((d1/a1),0.3333333);

            Log.e( "RESULT", "x = " + String.format("%.2f",x1) + " \nx = " + String.format("%.2f",x2) + "\nx = " + String.format("%.2f",x3)  );
            result+= "x = " + String.format("%.2f",x1) + " \nx = " + String.format("%.2f",x2) + "\nx = " + String.format("%.2f",x3);
            result+='\n';

        }
        else if(h<=0){         // all 3 roots are real
            i = Math.pow((((g*g)/4)-h),0.5);
            j = Math.pow(i,0.33333333333);
            k = Math.acos((g/(2*i))*-1);
            l = j * -1;
            m = Math.cos(k/3);
            n = Math.sqrt(3) * Math.sin(k/3);
            p = (b1/(3*a1))*-1;
            x1 = (2*j)*m-(b1/(3*a1));
            x2 = l * (m+n) + p;
            x3 = l * (m-n) + p;

            Log.e( "RESULT", "x = " + String.format("%.2f",x1) + " \nx = " + String.format("%.2f",x2) + "\nx = " + String.format("%.2f",x3)  );
            result+= "x = " + String.format("%.2f",x1) + " \nx = " + String.format("%.2f",x2) + "\nx = " + String.format("%.2f",x3);
            result+='\n';
        }
        else if(h>0){       // only 1 root is real
            int qq = 0 ;

            r = ((g/2)*-1)+Math.pow(h,0.5);
            if(r<0 )
            {
                r = r*-1 ;
                qq=1 ;
            }
            s = Math.pow(r,0.333333);
            if(qq ==1) s = s*-1 ;
            qq=0 ;

            t = ((g/2)*-1)-Math.pow(h,0.5);

            if(t<0 )
            {
                t = t*-1 ;
                qq=1 ;
            }
            u = Math.pow(t,0.3333);
            if(qq ==1) u = u*-1 ;

            x1 = (s+u) - (b1/(3*a1));

            Log.e( "RESULT", "x1 = " + String.format("%.2f",x1) );
            result+= "x1 = " + String.format("%.2f",x1);
            result+='\n';

            x2re = (((s+u)*-1)/2) - (b1/(3*a1));
            x2im = -(s-u)*Math.pow(3,0.5)/2;
            Log.e( "RESULT", "x2 = (" + String.format("%.2f",x2re )+ ",  " + String.format("%.2f", x2im) + "i )");
            result+= "x2 = (" + String.format("%.2f",x2re )+ ",  " + String.format("%.2f", x2im) + "i )";
            result+='\n';

            x3re = (((s+u)*-1)/2) - (b1/(3*a1));
            x3im = (s-u)*Math.pow(3,0.5)/2;
            Log.e( "RESULT", "x3 = (" + String.format("%.2f",x3re) + ",  " + String.format("%.2f",x3im) + "i )");
            result+= "x3 = (" + String.format("%.2f",x3re) + ",  " + String.format("%.2f",x3im) + "i )";
            result+='\n';

        }

    }


    private int[]  StraitLineEquation(String string)
    {
        String  Str = "" ;


        for (int i = 0; i < string.length() ; i++) {
            if(string.charAt(i)==' ')
                continue ;
            Str += string.charAt(i);

        }

        /*
        Log.e( "RESULT", Str);
        result+= Str;
        result+='\n';
        */

        int num =0, aa =0 ;
        int a1=0, b1=0, c1=0; boolean flag = false ;
        for(int i=0 ; i< Str.length(); i++)
        {

            if(Str.charAt(i)>='0' && Str.charAt(i)<= '9')
            {

                num *= 10 ;
                num += Str.charAt(i) -'0' ;
                aa  = num ;
                if(i>1)
                    if(Str.charAt(i-1) == '-')
                    {
                        aa = - num ;
                        flag = true ;
                    }
                if(flag)
                    aa = - num ;
            }

            else num =0 ;

            if(Str.charAt(i)=='x')
            {
                if(aa!=0)
                    a1 = aa ;
                else{
                    if(i>=1){
                        if ( Str.charAt(i-1)== '-')
                        {
                            a1 =-1 ;
                        }
                        else  a1 = 1 ;
                    } else a1 =1;

                }
                aa=0 ;
            }


            else if(Str.charAt(i)=='y')
            {
                if(aa!=0)
                    b1 = aa ;
                else{
                    if(i>=1){
                        if ( Str.charAt(i-1)== '-')
                        {
                            b1 =-1 ;
                        }
                        else  b1 = 1 ;
                    } else b1 =1;

                }
                aa=0 ;
            }

            else if(Str.charAt(i)== '=' )   c1 = -aa ;

            else if(Str.charAt(i)== '+' || Str.charAt(i)=='-') ;
            else
            {
                if(aa!=0)
                    c1 = aa ;

            }
        }

        final int[] arr = new int[10] ;
        arr[0] = a1 ;
        arr[1] = b1 ;
        arr[2] = c1 ;
        //   System.out.println(" a= "+ a1 + "  b = " + b1 +" c = " + c1 );


        return arr ;
    }

    private void  StratLineSolved(int n, int a1, int b1, int c1)
    {
        int i,j,k;
        float[][] a = new float[10][10] ;
        float[]  x = new float[10];
        float s,p;

        a[0][0] = a1 ;
        a[0][1] = c1 ;

        for(k=0; k<n-1; k++)
        {
            for(i=k+1; i<n; i++)
            {
                p = a[i][k] / a[k][k] ;
                for(j=k; j<n+1; j++)
                    a[i][j]=a[i][j]-p*a[k][j];
            }
        }
        x[n-1]=a[n-1][n]/a[n-1][n-1];
        for(i=n-2; i>=0; i--)
        {
            s=0;
            for(j=i+1; j<n; j++)
            {
                s +=(a[i][j]*x[j]);
                x[i]=(a[i][n]-s)/a[i][i];
            }
        }
        Log.e( "RESULT", "The result is: ");
        result+= "The result is: ";
        result+='\n';
        /*
        for(i=0; i<n; i++) {

            Log.e("RESULT", "x = " + String.format("%.2f", x[i]));
            result += "x = " + String.format("%.2f", x[i]);
            result += '\n';
        }
        */

    }

    private void  StratLineSolved(int n, int a1, int b1, int c1, int a2, int b2, int c2)
    {
        int i,j,k;
        float[][] a = new float[10][10] ;
        float[]  x = new float[10];
        float s,p;

        a[0][0] = a1 ;
        a[0][1] = b1 ;
        a[0][2] = c1 ;
        a[1][0] = a2 ;
        a[1][1] = b2 ;
        a[1][2] = c2;

        for(k=0; k<n-1; k++)
        {
            for(i=k+1; i<n; i++)
            {
                p = a[i][k] / a[k][k] ;
                for(j=k; j<n+1; j++)
                    a[i][j]=a[i][j]-p*a[k][j];
            }
        }
        x[n-1]=a[n-1][n]/a[n-1][n-1];
        for(i=n-2; i>=0; i--)
        {
            s=0;
            for(j=i+1; j<n; j++)
            {
                s +=(a[i][j]*x[j]);
                x[i]=(a[i][n]-s)/a[i][i];
            }
        }
        Log.e( "RESULT", "The result is: ");
        result += "The result is: ";
        result += '\n';

        Log.e( "RESULT", "x = " + String.format("%.2f",x[0] ));
        result += "x = " + String.format("%.2f",x[0] );
        result += '\n';

        Log.e( "RESULT", "y = " + String.format("%.2f",x[1] ) );
        result += "y = " + String.format("%.2f",x[1] );
        result += '\n';
    }

    String getResult(){
        return result;
    }

}