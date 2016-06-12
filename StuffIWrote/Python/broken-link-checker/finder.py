import sys

import httplib2
import requests
from BeautifulSoup import BeautifulSoup, SoupStrainer


def check(url):
    session = requests.Session()
    response = session.get(url)

    soups = BeautifulSoup(response.content, parseOnlyThese=SoupStrainer('a'))
    for a_tag in soups:
        try:
            href = str(a_tag['href'])
            if href.startswith('http'):
                try:
                    res = session.get(href)
                    if res.status_code == 200:
                        print href, 'worked'
                    else:
                        print 'BROKEN:', href, res.status_code
                except httplib2.RedirectLimit:
                    print 'skipping', href, 'too many redirects'
                except requests.exceptions.ConnectionError:
                    print 'skipping', href, 'could not connect'

        except KeyError:
            # print 'skipping', a_tag, 'no href'
            pass


if __name__ == '__main__':
    check(sys.argv[1])
