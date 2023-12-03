#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <sstream>
#include <memory>
#include <map>

using namespace std;


/** Sure, it ain't exactly a speed-demon. But it does work. */

struct Tag {
    bool isClosingTag;
    string name = "";
    vector<shared_ptr<Tag>> children;
    map<string, string> attributes;
};

class TagParser {
    string baseString;
    int idx = 0;

    shared_ptr<Tag> theTagWeAreParsing = make_shared<Tag>();

public:
    static shared_ptr<Tag>
    parse(const string& inputString, const shared_ptr<Tag> parentTag) {
        shared_ptr<Tag> newTag = TagParser(inputString).parse();
        if (!newTag->isClosingTag && parentTag != nullptr) {
            parentTag->children.push_back(newTag);
        }
        return newTag;
    }

private:
    TagParser(const string& inputString) {
        baseString = inputString;
    }

    void parseName() {
        while (currentChar() != ' ' && currentChar() != '>') {
            theTagWeAreParsing->name += advance(/* letter */);
        }
    }

    string parseKey() {
        string key;
        while (currentChar() != ' ') {
            key += advance(/* letter */);
        }
        return key;
    }

    string parseValue() {
        string value;
        while (currentChar() != '\"') {
            value += advance(/* letter */);
        }
        advance(/* close-quote */);
        return value;
    }

    void parseAttribute() {
        string key = parseKey();
        idx += string(" = \"").size();
        string value = parseValue();
        theTagWeAreParsing->attributes.emplace(key, value);
    }

    void parseAttributes() {
        if (!theTagWeAreParsing->isClosingTag) {
            while (advance() == ' ') {
                parseAttribute();
            }
        }
    }

    void parseHeader() {
        advance(/* left brace */);
        if (currentChar() == '/') {
            theTagWeAreParsing->isClosingTag = true;
            advance(/* closing slash */);
        }
    }

    shared_ptr<Tag> parse() {
        if (currentChar() == '<') {
            parseHeader();
            parseName();
            parseAttributes();
        }
        return theTagWeAreParsing;
    }

    char currentChar() {
        return baseString[idx];
    }


    char advance() {
        return baseString[idx++];
    }
};

class Query {
    int idx = 0;
    string queryString;
    shared_ptr<Tag> currentNode;

public:
    Query(const string& inputLine, const shared_ptr<Tag> domRoot) {
        queryString = inputLine;
        currentNode = domRoot;
    }

    static string respondTo(const string& inputLine, const shared_ptr<Tag> domRoot) {
        return Query(inputLine, domRoot).respond();
    }

private:

    string readSegment() {
        string querySegment;
        while (currentChar() && currentChar() != '~' && currentChar() != '.') {
            querySegment += advance();
        }
        return querySegment;
    }

    bool moveToChild(const string& key) {
        for (const shared_ptr<Tag> child : currentNode->children) {
            if (child->name.compare(key) == 0) {
                currentNode = child;
                return true;
            }
        }
        return false;
    }

    string respond() {
        // go to the right node
        do if (!moveToChild(readSegment())) return "Not Found!";
        while (advance() == '.');

        // get the attribute value
        const string key = readSegment();
        const auto iterator = currentNode->attributes.find(key);
        if (iterator == currentNode->attributes.end()) {
            return "Not Found!";
        } else {
            return iterator->second;
        }
    }

    char currentChar() {
        return queryString[idx];
    }

    char advance() {
        return queryString[idx++];
    }
};

int main() {

    // read input metadata
    int numLinesOfCode, numQueries;
    string inputLine;
    getline(cin, inputLine);
    stringstream(inputLine) >> numLinesOfCode >> numQueries;

    // create a root
    shared_ptr<Tag> currentTag = TagParser::parse("root-node", nullptr);
    vector<shared_ptr<Tag>> tagStack = {currentTag};

    // parse the tag tree
    for (int lineIdx = 0; lineIdx < numLinesOfCode; lineIdx++) {
        getline(cin, inputLine);
        shared_ptr<Tag> newTag = TagParser::parse(inputLine, currentTag);
        if (newTag->isClosingTag) {
            tagStack.pop_back();
        } else {
            tagStack.push_back(newTag);
        }
        currentTag = tagStack[tagStack.size() - 1];
    }

    // respond to the queries
    for (int queryIdx = 0; queryIdx < numQueries; queryIdx++) {
        getline(cin, inputLine);
        cout << Query::respondTo(inputLine, currentTag) << endl;
    }
    return 0;
}
