package com.example.completablefuture;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

public class CompletableFutureApplication {

  public static Map<Student, Double> studentsAverage = new ConcurrentHashMap<>();
  @AllArgsConstructor
  public static class Student {
    private String name;
  }
  @AllArgsConstructor
  public static class AverageRunnable implements Runnable{
      private Student student;
      private List<Double> marksList;

      @Override
      public void run() {
          studentsAverage.put(
                  student,
                  marksList.stream().reduce(0., Double::sum)
                          / marksList.size());
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
    List<CompletableFuture<Void>> completableFutures = students.stream()
            .map(student -> CompletableFuture.runAsync(new AverageRunnable(student, studentsMarks.get(student))))
            .collect(Collectors.toList());
    CompletableFuture.allOf(completableFutures.get(0), completableFutures.get(1),
            completableFutures.get(2), completableFutures.get(3), completableFutures.get(4));
    students.forEach(
        student -> System.out.println(student.name + " " + studentsAverage.get(student)));
  }
}
