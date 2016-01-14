All development for Vellum should be done in
SecureAppGenerator/thirdparty/Vellum/src

Then run the script 
SecureAppGenerator/minify_Vellum.sh using shell command
sh minify_Vellum.sh
NOTE: this could be added to the preBuild.ant script to be done automatically
but it takes a long time to minify all the javascript files, so it is only needed during
Vellum development.

Then Clean the project and Rebuild the Project.  
The changes made will be copied over via preBuild.ant to 
SecureAppGenerator/src/main/resources/static/Vellum
