# DevTrack Startup Script
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host " Starting DevTrack Growth Platform (Port 8085)" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$env:JAVA_HOME = "C:\Program Files\Java\jdk-22"
$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path

Set-Location $baseDir

& "C:\apache-maven-3.9.15\bin\mvn.cmd" spring-boot:run
