/**********************************************************************************
   Adapted Large Factorial C program into JNI format for Android application.
   Original Source:
   https://github.com/iankits/my_random_projects/tree/master/big_factorial_prog

   @Author Ankit Singh
   @copyright DevGeeks Lab, 2014
   The license of the project comes with code.
 **********************************************************************************/
#include <jni.h>
#include <android/log.h>

#define max 999999
#define LOG_TAG "factoLib"

jintArray a[max];
jlong no, prev_len;

typedef struct resultCont {
	jintArray result;
	jlong size;
} resCont;

JNIEXPORT jlong JNICALL Java_com_devgeekslab_calcfactorial_MainActivity_getSize(JNIEnv *pEnv, jobject pObj) {
	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Returning:: %ld", prev_len);
	return prev_len;
}

JNIEXPORT jintArray JNICALL Java_com_devgeekslab_calcfactorial_MainActivity_getFactorial(JNIEnv *pEnv, jobject pObj, jlong input) {
	memset(a, 0, max*sizeof(*a));
	jintArray result;
	result = (*pEnv)->NewIntArray(pEnv, max);

	long temp=0, len,s,i,t,j,r,q,len_index;
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
		}
	}

	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Final Factorial Result length %ld", prev_len);
	// Print the Calculated Factorial
	/*temp=0;
	for(i=prev_len;i>=0;i--){
		if((a[i]!=0) || (temp!=0)){
			__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "%d", (jint) a[i]);
			temp=1;
		}
	}*/

	// move from the a structure to the java structure
	(*pEnv)->SetIntArrayRegion(pEnv, result, 0, prev_len+1, a);
	(*pEnv)->DeleteLocalRef(pEnv, a);
	return result; // Return factorial as a result
}

