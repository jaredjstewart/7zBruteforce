/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import static java.util.stream.Collectors.joining;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.math3.util.Combinations;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Seven7Decompressor {
  private static File file = new File("/Users/jstewart/Documents/Compressed\\ file\\ 2.7z");


  private static String[] stubs = Stream.of("some", "strings", "to", "try", "jared", "this", "is", "a", "test").toArray(String[]::new);

  public static SevenZFile decompress(File file, String password) throws IOException {
    byte[] passwordBytes = password.getBytes(Charsets.UTF_16LE);
    return new SevenZFile(file, passwordBytes);
  }

  public static SevenZFile tryPasswordCombinations(int k) {
    Combinations combinations = new Combinations(stubs.length, k);

    for (int[] c : combinations) {
      String password = Arrays.stream(c).mapToObj(i -> stubs[i]).collect(joining(""));

      try {
        SevenZFile sevenZFile = decompress(file, password);
        System.out.println("\n\n\n ******* SUCCCESSSS *******");
        System.out.println("Password: " + password);
        return sevenZFile;
      } catch (IOException e) {
        System.out.println(password + " failed");
      }
    }

    return null;
  }

  public static SevenZFile crack(File file) {
    return IntStream.range(1, 10)
        .mapToObj(Seven7Decompressor::tryPasswordCombinations)
        .filter(Objects::nonNull)
        .findFirst().orElseThrow(() -> new RuntimeException("No combination worked"));
  }


  public static void main(String[] args) {
    crack(file);
  }
}
