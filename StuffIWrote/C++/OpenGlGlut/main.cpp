#include <iostream>

// GLEW lets us access modern OpenGL API even from old OpenGL version

#include <GL/glew.h>

// GLUT is an older way of doing cross-platform window and input management
#include <GLUT/glut.h>

using namespace std;

int main(int argc, char** argv) {
    // init GLUT and create Window
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);
    glutInitWindowPosition(100,100);
    glutInitWindowSize(320,320);
    glutCreateWindow("Lighthouse3D- GLUT Tutorial");

    return 1;
}
