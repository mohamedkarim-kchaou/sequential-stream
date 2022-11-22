package com.example.parallelstream;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;

public class ParallelStreamApplication {
  @AllArgsConstructor
  public static class Student {
    private String name;
  }

  public static void main(String[] args) {
    Student firstStudent = new Student("firstStudent");
    Student secondStudent = new Student("secondStudent");
    Student thirdStudent = new Student("thirdStudent");
    Student fourthStudent = new Student("fourthStudent");
    Student fifthStudent = new Student("fifthStudent");
    List<Student> students =
        List.of(firstStudent, secondStudent, thirdStudent, fourthStudent, fifthStudent);
    Map<Student, List<Double>> studentsMarks = new ConcurrentHashMap<>();
    studentsMarks.put(firstStudent, List.of(1., 2., 3., 4., 5.));
    studentsMarks.put(secondStudent, List.of(0., 0., 1., 1., 2.));
    studentsMarks.put(thirdStudent, List.of(1., 4., 5., 5., 5.));
    studentsMarks.put(fourthStudent, List.of(1., 2.5, 3.5, 4., 0.));
    studentsMarks.put(fifthStudent, List.of(5., 2., 3., 5., 5.));
    Map<Student, Double> studentsAverage = new ConcurrentHashMap<>();
    students.parallelStream().forEach(
        student ->
            studentsAverage.put(
                student,
                studentsMarks.get(student).parallelStream().reduce(0., Double::sum)
                    / studentsMarks.get(student).size()));
    students.parallelStream().forEach(
        student -> System.out.println(student.name + " " + studentsAverage.get(student)));
  }
}
