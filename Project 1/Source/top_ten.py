import sys
import json
import operator

def hw():
    print ('Hello, world!')

def lines(fp):
    print(str(len(fp.readlines())))

def freq(file):
    data={}

    with open(file) as fp:
      for line in fp:
        if 'created_at' in json.loads(line).keys():
            a=json.loads(line)['entities']['hashtags']

            for word in a:
                if word['text'].encode('utf-8') in data.keys():
                   data[word['text'].encode('utf-8')]+=1;
                else:
                   data[word['text'].encode('utf-8')]=1
    d= sorted(data.items(), key=operator.itemgetter(1),reverse=True)
    #print d
    for i in range(0,10):
        print(d[i][0],d[i][1])


def main():
    tweet_file = open(sys.argv[1])
    freq(sys.argv[1])


if __name__ == '__main__':
    main()
