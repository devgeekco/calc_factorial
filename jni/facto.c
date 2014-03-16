/**********************************************************************************
   Adapted Large Factorial C program into JNI format for Android application
   @Author Ankit Singh, 2014
   @copyright DevGeeks Lab
 **********************************************************************************/
#include <jni.h>
#include <android/log.h>

#define max 999999
#define LOG_TAG "factoLib"

jintArray a[max];
jlong no;

JNIEXPORT jintArray JNICALL Java_com_devgeekslab_calcfactorial_MainActivity_getFactorial(JNIEnv *pEnv, jobject pObj, jlong input) {
	memset(a, 0, max*sizeof(*a));
	long temp=0, len,s,i,t,j,r,q, prev_len,len_index;
	a[0]= (jint) 1;

	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "To Find factorial of %jd", input);
	no=input;
	a[0]=1;
	prev_len = len_index =0;

	for(i=1; i<=no; i++) {
		r=0;
		q=0;
		len=0;
		temp=0;

		if((prev_len != 0)) {
			if(prev_len != (max-1))
				len_index=prev_len+6;
			else
				len_index=max-1;
		} else
			len_index = 1; // for the first time into the loop

		// This loop is check how many array length is in used in calculation.
		//   eg. array has a[0]=x, a[1], a[3]=y values,
		//   this means till now program has utilized 3 spaces,
		//   so len will return 3
		for(s=len_index;s>=0;s--) {
			if((a[s]!=0)|| (temp!=0)){
				len++;
				// DEBUG
				//printf("\ni=%ld, s=%ld, prev_len=%ld, len=%ld",i,s,prev_len,len);
		//		__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "i=%ld, s=%ld, prev_len=%ld, len=%ld",i,s,prev_len,len);
				prev_len = len;
				temp=1;
			}
		}

		for(j=0;j<(len+6);j++){
			t= (long) a[j]*i+q; // The main formula for solution. Explanation given below

			// r is used to save the last digit.
			// taking example that t = 13.
			// r = 13 % 10
			// r = 3
			// then r is saved on the array
			r=(jint) t % 10;

			// q is used to save the first digit.
			//	taking example that t = 13.
			// q = 13 / 10
			// q = 1
			// then q is saved on the array
			q=(jint)t/10;

			a[j]=r; //inserting the value into the array

			// DEBUG
			//      printf("\n\tj=%ld, t(a[j]*i+q)=%ld, r(t%%10)=%ld, q(t/10)=%ld a[%ld]=%d",j,t,r,q,j,a[j]);
		}
	}

	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Final Factorial Result length %ld", prev_len);
	// Print the Calculated Factorial
	temp=0;
	// printf("\nFactorial --> ");
	for(i=prev_len;i>=0;i--){
		if((a[i]!=0) || (temp!=0)){
			__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "%d", (jint) a[i]);
			// printf("%d", a[i]);
			temp=1;
		}
	}
	//printf("\n"); */
	return a;
	//return 0;
}

