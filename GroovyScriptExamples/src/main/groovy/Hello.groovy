
import org.apache.nifi.flowfile.*
import org.apache.nifi.processor.*
import org.apache.nifi.processor.exception.ProcessException
import org.apache.nifi.processor.io.OutputStreamCallback

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

def onTrigger(final ProcessContext context, final ProcessSession session) throws Exception {
    
    FlowFile incomingFlowFile = session.get();
    if (incomingFlowFile == null) {
        return;
    }
    
    FlowFile copy = session.clone(incomingFlowFile);
    copy = session.putAttribute(copy, "script.attrib", "copy");
    callBack = { OutputStream out -> out.write("\nHello, Groovy!".getBytes()) } as OutputStreamCallback;
    copy = session.append(copy, callBack);
    session.getProvenanceReporter().modifyContent(copy, "Appended 'Hello, Groovy!'");
    
    incomingFlowFile = session.putAttribute(incomingFlowFile, "script.attrib", "original");
    
    return [A:incomingFlowFile, B:copy];
}