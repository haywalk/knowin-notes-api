# Knowin' Notes REST API

This is the backend for [Knowin' Notes](https://github.com/haywalk/knowin-notes), a web application intended to teach the user how to sight read piano music. This application was developed as part of COMP 4721 (Software Design) at Mount Allison University in Fall 2024, and I have mirrored the repository here for posterity. The API is written in Java using Spring, with Gradle as the build system.

## Installation

Requires Gradle 8.10.2, Java 17, and `pdflatex` to be installed. Run using `gradle bootRun`.

## API Specification

<dl>
  <dt>/api/LIST_REPORTS</dt>
  <dd>Returns a JSON array containing all reports in the database as JSON objects.</dd>   

  <dt>/api/GET_REPORT?id=<code>id</code></dt>
  <dd>Return the report with ID <code>id</code> as a JSON object.</dd>  

  <dt>/api/GET_STATE?old=<code>state</code></dt>
  <dd>Given a JSON representation of a state encoded in base-64, return either an updated state as plain JSON or a report as plain JSON if the game has ended.</dd>   
  
  <dt>/api/GENERATE_PDF?id=<code>id</code></dt>
  <dd>Return a PDF document containing the report with ID <code>id</code>.</dd>  
</dl>
