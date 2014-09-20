#include<stdio.h>
#include<signal.h>
#include<stdlib.h>

static int NUMPROCESSES;
static pid_t *processIds;

// start @numProcesses number of processes
// store them in (pid_t *processIds)
int start(int numProcesses)
{
    int i, j;

    // don't start more than once
    if (NUMPROCESSES > 0)
    {
        return -1;
    }

    // set the given number of processes
    NUMPROCESSES = numProcesses;

    // allocate space on heap for enough pids for each process
    processIds = malloc(NUMPROCESSES * sizeof(pid_t));
    if(processIds == NULL)
    {
        printf("Unable to allocate space for processId array.\n");
        return -1;
    }

    // for each process
    for(i = 0; i < NUMPROCESSES; i++)
    {
        // fork
        pid_t retValue = fork();

        // if failed, terminate everyone already created
        if(((int)retValue) < 0)
        {
            printf("Failed to fork child process, killing others...\n");
            for(j = 0; j < i; j++)
            {
                if(kill(processIds[j], SIGINT) == -1)
                {
                    printf("Failed to kill process with id %d\n", ((int)processIds[i]));
                }
            }
            return -1;
        }

        // if child, spin forever
        if(((int)retValue) == 0)
        {
            while(1);
        }

        // if parent, capture pid and continue
        else
        {
            processIds[i] = retValue;
        }
    }
    return 0;
}

// kill all the processes
int stop()
{
    int i;
    for(i = 0; i < NUMPROCESSES; i++)
    {
        if(kill(processIds[i], SIGINT) < 0)
        {
            printf("Failed to kill process with id %d\n", ((int)processIds[i]));
            return -1;
        }
    }
    NUMPROCESSES = 0;
    return 0;
}
