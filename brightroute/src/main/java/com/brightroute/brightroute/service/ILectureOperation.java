package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Lecture;

public interface ILectureOperation {
    Lecture addLecture(Lecture lecture);
    Lecture updateLecture(Long lectureId, Lecture updated);
    void deleteLecture(Long lectureId);
}
