RoboticsImageProcessor
======================

This program is an attempt to find the robot's camera pose (and the robot's orientation from that) in a 3D space using two vision targets of known dimension and position on a fixed, veritical 2D plane.

List of requirements:
- [x] Edge and corner detection of the vision target rectangles on a single test image
- [ ] Get camera image from IP camera (http? more research needed)
- [ ] Use homologic transforms to figure out relative translation of viewed vision targets to real vision targets
- [ ] Use camera calibration matrix and homologic math to map homography H to to camera pose K[R|t] see [link](http://dsp.stackexchange.com/questions/2736/step-by-step-camera-pose-estimation-for-visual-tracking-and-planar-markers) for info on this method
- [ ] Open tunnel to cRIO via [Google Protocol Buffers](https://developers.google.com/protocol-buffers/)
- [ ] Use Google Protocol Buffers to move camera to seek vision targets when outside of range 
