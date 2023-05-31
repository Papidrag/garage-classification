1.Pytorch代码：

train.ipynb 为基于Mobile-ViT垃圾分类系统的原型系统，其中包含代码以及详细的运行结果。

compare.ipynb、vgg16.ipynb、resnet50.ipynb 为用来Mobile-ViT、VGG16、ResNet50三个模型对比的代码，同样页包含详细的运行结果。

 

2.Android代码

MainActivity.java 为所设计Android系统的功能逻辑代码。

activity_main.xml 为所设计Android系统的界面代码。

 

3.数据集

Class 文件夹为在训练过程中所用的数据集，其中包括Recyclable、Harmful、Kitchen、Other和evalImageSet，每个分类文件夹的图片大概为16000张，验证图片大概为5000张，因为图片占用内存太大，本附件仅上传其文件夹索引目录。

 

4.图片

模型结构图.pptx  文件包含Mobile-ViT模型整体结构、Mobile-ViT Block结构图。

loss.png 为原型系统结果的损失值变化过程图。

原型结果.png 为原型系统对验证图片的预测效果图。

Mobile-ViT训练结果.png 为Mobile-ViT模型训练的数值型结果图。

VGG16训练结果.png 为VGG16模型训练的数值型结果图。

ResNet50训练结果.png 为ResNet50模型训练的数值型结果图。

各模型大小.png 为三个模型的大小对比图。

1.png、2.png、3.png 为在App种对三种垃圾预测演示图。

内存.png 为垃圾分类App在手机中占用内存。

 