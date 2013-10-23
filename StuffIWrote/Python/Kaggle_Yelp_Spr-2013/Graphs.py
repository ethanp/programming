import matplotlib.pyplot as plt

# histogram of the 'helpful' scores of the reviews in the training set
def helpfulDist(reviewsList):
    helpful = [review['votes']['useful'] for review in reviewsList]
    # helpful = [0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]
    plt.hist(helpful, bins=(0,1,2,3,4,5,6,7,8,9,10))
    plt.xlabel('number of useful votes')
    plt.ylabel('count at this number')
    plt.title('distribution of useful votes')
    plt.axis([0,10,0,100000])
    plt.show()



if __name__ == "__main__":
    helpfulDist(None)
