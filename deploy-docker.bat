@echo off
echo ==============================================================================
echo DevTrack - Building & Launching Docker Production Stack
echo ==============================================================================
docker-compose down
docker-compose up -d --build
echo.
echo Application is starting at http://localhost:8085
echo Check status using: docker-compose ps
echo View logs using:   docker-compose logs -f
pause
