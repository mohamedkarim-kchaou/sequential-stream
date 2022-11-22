package com.example.executorservice;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

public class ExecutorServiceApplication {

  public static Map<Student, Double> studentsAverage = new ConcurrentHashMap<>();
  @AllArgsConstructor
  public static class Student {
    private String name;
  }
  @AllArgsConstructor
  public static class AverageRunnable implements Callable<Void>{
      private Student student;
      private List<Double> marksList;

      @Override
      public Void call() {
          studentsAverage.put(
                  student,
                  marksList.stream().reduce(0., Double::sum)
                          / marksList.size());
          return null;
      }
  }

  public static void main(String[] args) throws InterruptedException {
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
    List<Callable<Void>> tasks = students.stream().map(student -> new AverageRunnable(student, studentsMarks.get(student))).collect(Collectors.toList());
    ExecutorService executorService = Executors.newFixedThreadPool(students.size());
    executorService.invokeAll(tasks);
    students.parallelStream().forEach(
        student -> System.out.println(student.name + " " + studentsAverage.get(student)));
    executorService.shutdown();
  }
}
