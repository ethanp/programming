latex input:		mmd-article-header
Title:		Matlab Notes
Author:		Ethan C. Petuchowski
Base Header Level:		1
latex mode:		memoir
Keywords:		Matlab, Syntax, Machine Learning, Coursera, Andrew Ng, Octave
CSS:		http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:			2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

## Syntax Notes

### Indexing

1. Access first element (works on row or column vector)
        
        myVector(1)
        
2. Matrices are indexed by **row, *then* column**
    * E.g. to get **all rows of column 1**
    
            myMatrix(:, 1)

### Functions

#### Function Declaration

The function declaration outline looks like

    function [A, B] = warmUpExercise(param1, param2)
        % ... my code
    end

This would indicate the function will **return** two variables to be defined within the function, under the names `A` and `B`, and it takes two **parameters**.
### For loops

[From the matlab docs](http://www.mathworks.com/help/matlab/ref/for.html)

Create a Hilbert matrix using nested for loops:

    k = 10;
    hilbert = zeros(k,k);      % Preallocate matrix
    
    for m = 1:k
        for n = 1:k
            hilbert(m,n) = 1/(m+n -1);
        end
    end
     
Step by increments of -0.1, and display the step values:

    for s = 1.0: -0.1: 0.0
       disp(s)
    end
 
Execute statements for a defined set of index values:

    for s = [1,5,8,17]
       disp(s)
    end
 
Successively set e to unit vectors:

    for e = eye(5)
      disp('Current value of e:')
      disp(e)
    end

### Other
#### Silence the output of each line like an interpreter

The results from each executed line are *printed by default*, to silence this output, **terminate the line with a semicolon**.

## Useful Functions

### Load data
Load data from a csv

    load('ex1data1.txt');

### Summarize Matrices

#### Find lengths

Get the length of a vector

    length(myVector)
    
Get the **number of rows** or **columns** of a matrix

> do NOT use length(myMatrix)

    rows(myMatrix)   
    size(myMatrix, 1)  % alternative
    
    columns(myMatrix)
    size(myMatrix, 2)  % alternative
    
Get the length of **the longer of** the number of `rows` & `cols`

    length(myMatrix)
    
Get the dimensions of a matrix

    [rows cols] = size(myMatrix)

Get the **number of elements** in a matrix (i.e. rows X cols)

    numElements = numel(myMatrix)


#### Find summary stats

Find **mean/std-dev of each column of `A`**

	mean(A)
	std(A)
	
Find mean of each **row** of `A`

	mean(A, 2)

### Create useful matrices/vectors
Create a `rows` by `cols` matrix of `zeros`

    zeros(rows, cols)

Create the **identity matrix** of size `5`

	eye(5)

Create the **complex transpose** of `A`

    A'

Create the (pseudo) **inverse matrix** of `A`

	pinv(A)

Create a `rows x cols` **matrix of all ones**

	ones(rows, cols)

Create the vector `[1 2 3 4]`

	1:4
	linspace(1, 4, 4)

Create `N` elements, evenly spaced in range `[a,b]`

	linspace(a, b, N)

Turn a vector into a matrix

    >> reshape(1:6,2,3)    
    ans =
       1   3   5
       2   4   6

Turn a matrix into a vector

    >> [1 3 5; 2 4 6](:)
    ans =
       1
       2
       3
       4
       5
       6

The above two allow us to state the following

    >> reshape(myMat(:),rows(myMat),columns(myMat)) == myMat

## Plotting

### Most basic plotting

    % open a new figure window
    figure
    
    % Plot the data as red crosses of size 10
    plot(x, y, 'rx', 'MarkerSize', 10)
    
    % plot multiple lines
    plot(x, y1, x, y2)
    
    % plot using dashed line
    plot(x, y, '--')
    
    % plot multiple lines in multiple styles
    plot(x, y1, '--', x, y2, ':')

    % with specified color
    plot(x, y1, '--g', x, y2, ':r')
    
    % with specified markers of data locations
    plot(x, y1, '--go', x, y2, ':r*')
    
    % only plot the marker of the data point
    plot(x, y, '*')
    
#### Adjusting the plot

    % Set the y−axis label
    ylabel('Profit in $10,000s');
    
    % Set the x−axis label
    xlabel('Population of City in 10,000s');
    
    % Set the title lable
    title('This title has a \pi in it')
    
    % Add a legend to a plot with two lines.
    % The formatting of that line will be correlated
    % with the legend item by index.
    legend('y = sin(x)', 'y = cos(x)')
    
    % Move it to the bottom left
    legend('y = sin(x)', 'y = cos(x)', 'Location', 'southwest')
        
    % Display the current colormap in the figure
    colorbar

### Interesting plot types

[3D] Surface plots

	surf(x, y, z, gradient(z))
	
Contour plots

	contour(x, y, z)