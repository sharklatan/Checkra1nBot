echo "----------------------"
echo "Starting script..."
echo "----------------------"

$jar_version = Read-Host -Prompt "`nJAR Version"

mvn clean package

$output_dir = '.\Checkra1nBot'

New-Item -Force -ItemType directory -Path $output_dir

Copy-Item -Path .\target\Checkra1nBot-"$jar_version"-shaded.jar -Destination "$output_dir"
Copy-Item -Path .\blocked-accounts.txt -Destination "$output_dir"
Copy-Item -Path .\blocked-words.txt -Destination "$output_dir"
Copy-Item -Path .\search-keys.txt -Destination "$output_dir"

echo `n'Copied Files'
echo `n'Sending to server'

scp -r $output_dir pi@192.168.1.170:~/Desktop

Remove-Item -Recurse -Force $output_dir

echo `ndone