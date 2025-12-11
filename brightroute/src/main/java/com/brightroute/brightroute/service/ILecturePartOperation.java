package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.LecturePart;

public interface ILecturePartOperation {
    LecturePart addPart(LecturePart part);
    LecturePart updatePart(Long partId, LecturePart updated);
    void deletePart(Long partId);
}
