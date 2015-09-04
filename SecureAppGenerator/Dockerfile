FROM localhost:5000/tomcat:8-jdk8

MAINTAINER Charles LaPierre <charlesl@benetech.org>

WORKDIR /usr/local

USER root

ENV BUILD_PACKAGES curl unzip 
ENV DEV_PACKAGES git nano
ENV TERM vt220

RUN apt-get update \
	&& apt-get --yes install $BUILD_PACKAGES $DEV_PACKAGES

#Install Gradle
ENV GRADLE_URL https://downloads.gradle.org/distributions/gradle-2.5-all.zip
RUN curl -SL "$GRADLE_URL" -o gradle.zip \
       	&& unzip gradle.zip \
       	&& mv gradle-2.5 gradle
ENV GRADLE_HOME /usr/local/gradle
ENV PATH $PATH:$GRADLE_HOME/bin
RUN rm gradle.zip

# Support Gradle
ENV JAVA_OPTS -Xms256m -Xmx512m
  
# Installs Android SDK
ENV ANDROID_SDK_FILENAME android-sdk_r24.3.3-linux.tgz
ENV ANDROID_SDK_URL http://dl.google.com/android/${ANDROID_SDK_FILENAME}
ENV ANDROID_API_LEVELS android-17,android-19,android-20 
ENV ANDROID_BUILD_TOOLS_VERSION 22.0.1
ENV ANDROID_HOME /usr/local/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools
ENV ANDROID_PACKAGES libc6-i386 lib32stdc++6 lib32gcc1 lib32ncurses5 lib32z1

# Install Android SDK
RUN apt-get --yes install $ANDROID_PACKAGES 
RUN wget --output-document=android-sdk.tgz --quiet ${ANDROID_SDK_URL} 
RUN tar xzf android-sdk.tgz \
	&& rm -f android-sdk.tgz \
	&& chown -R root.root android-sdk-linux \
	&& chgrp tomcat /usr/local/tomcat \
	&& chmod g+w /usr/local/tomcat

# Install Android SDK components
ENV ANDROID_SDK_COMPONENTS platform-tools,build-tools-22.0.1,\
	android-22,extra-android-support,extra-android-m2repository
RUN echo y | android update sdk --no-ui --all --filter "${ANDROID_SDK_COMPONENTS}"

# Install FDroid
ENV FDROID_HOME /usr/local/fdroidserver
ENV PATH $PATH:$FDROID_HOME
ENV FDROID_PACKAGES python-paramiko python-imaging python-magic python-libcloud\
	python-pyasn1 python-pyasn1-modules python-requests python-libcloud/trusty-backports
RUN git clone https://gitlab.com/fdroid/fdroidserver.git \
	&& sed -i 's,^#[# ]*\(deb .*-backports.*\),\1 universe multiverse,'  /etc/apt/sources.list \
	&& apt-get update \
	&& apt-get install --yes $FDROID_PACKAGES

    
RUN apt-get purge --yes --auto-remove $BUILD_PACKAGES

# Cleaning
RUN apt-get clean

ENV ROOT_WAR_PATH /usr/local/tomcat/webapps/ROOT.war

COPY ./build/libs/SAG-*.war $ROOT_WAR_PATH

USER tomcat
