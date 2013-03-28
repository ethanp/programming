/*
 1/13/13
 Exercise 1-13: 
 Write a program to print a histogram of the lengths of words in its input.
      It is easy to draw the histogram with the bars horizontal;
          a vertical orientation is more challenging.

 Per:  https://discussions.apple.com/thread/2361809?start=0&tstart=0
  one must type a <C-d> to send the true EOF integer to the Terminal
      I was using <C-c>, but that actually sends an 'interrupt' signal

 Yes, this answer could be more compact, but why bother
    This language is a pain in the butt enough as it is
*/

#include <stdio.h>
#define MAXLEN 20 // this is ALL you need to change to fit longer words

int main()
{
    // we must hold characters in an int because EOF is an int
    int i, j, character, lenThisWord, myMaxLen, modeLength, lenCounter;
    myMaxLen = MAXLEN;
    lenThisWord = 0;
    modeLength = 0;

    // keep counts in an array
    int wordLengths[ MAXLEN + 1 ];
    for (i = 0; i <= MAXLEN; i++) {
        wordLengths[i] = 0;
    }

    // read input
    while ((character = getchar()) != EOF) {
        if (character == ' ' || character == '\t' || character == '\n') {
            // realllllllly long worrds default to MAXLEN
            if (lenThisWord > MAXLEN)
                lenThisWord = MAXLEN;
            wordLengths[lenThisWord]++;
            lenThisWord = 0;
        }
        else { 
            lenThisWord++; 
        }
    }

    // find max input word length
    while (!wordLengths[myMaxLen--]);
    // bit kludgy, but it's kinda cool
    myMaxLen++;

    /* print histogram horizontally */
    printf("\n\n");
    printf("Horizontal Representation:\n");
    printf("--------------------------\n");
    for (i = 1; i <= myMaxLen; i++) {
        printf("%2d:|", i);
        for (j = 0; j < wordLengths[i]; j++) {
            printf("#");
        }
        printf("\n");
    }
    
    printf("\n\n");

    /* print histogram vertically (way easier than I thought it'd be) */
    printf("Vertical Representation:\n");
    printf("------------------------\n");
    // find mode length
    for (i = 1; i <= myMaxLen; i++) {
        if (wordLengths[i] > modeLength)
            modeLength = wordLengths[i];
    }

    // print histogram bars
    lenCounter = modeLength;
    while (lenCounter) {
        printf("%2d:|", lenCounter);
        for (i = 1; i <= myMaxLen; i++) {
            if (wordLengths[i] >= lenCounter)
                printf(" #");
            else
                printf("  ");
        }
        printf("\n");
        lenCounter--;
    }

    // print bottom label
    printf("    ");
    for (i = 1; i <= myMaxLen; i++) {
        printf("--");
    }
    printf("\n");
    printf("    ");
    for (i = 1; i <= myMaxLen; i++) {
        printf("%2d", i);
    }
    printf("\n");

    return 0;
}

