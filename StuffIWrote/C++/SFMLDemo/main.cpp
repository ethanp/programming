#include <SFML/Graphics.hpp>
#include <iostream>
#include <unistd.h>
#include <GL/glew.h>

#define SIZE 800

using namespace std;

string getCwd();

int main() {

    sf::RenderWindow window(sf::VideoMode(640, 480, 32), "Hello SFML");

    sf::Font font;
    font.loadFromFile(getCwd() + "/" + "OpenSans-Bold.ttf");

    glClearDepth(1.f);
    sf::Text text("Hello World", font, 11);
    text.setCharacterSize(32);
    text.setFillColor(sf::Color::White);
    text.setPosition(window.getSize().x / 2 - text.getGlobalBounds().width / 2,
                     window.getSize().y / 2 -
                     text.getGlobalBounds().height / 2);

    while (window.isOpen()) {

        sf::Event event;
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
            }

            window.clear(sf::Color::Black);
            window.draw(text);
            window.display();
        }
    }
    return 0;
}

string getCwd() {
    char buffer[SIZE];
    char* answer = getcwd(buffer, sizeof(buffer));
    string s_cwd;
    if (answer) {
        s_cwd = answer;
    } else {
        cout << "Native function getcwd() failed: " << strerror(errno) << endl;
    }
    return s_cwd;
}
