rm -rf ~/r-test-temp
mkdir ~/r-test-temp
Rscript command-process-one-image.R ./test.jpg ~/r-test-temp '{"ROI.fill.thr":"7","stained.thr":"0.7"}'

