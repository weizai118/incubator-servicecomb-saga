/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package io.servicecomb.saga.alpha.server;

import java.io.IOException;
import java.util.Arrays;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

class GrpcStartable implements ServerStartable {

  private final Server server;

  GrpcStartable(int port, BindableService... services) {
    ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
    Arrays.stream(services).forEach(serverBuilder::addService);
    server = serverBuilder.build();
  }

  @Override
  public void start() {
    Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

    try {
      server.start();
      server.awaitTermination();
    } catch (IOException e) {
      throw new IllegalStateException("Unable to start grpc server.", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}