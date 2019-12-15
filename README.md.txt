Dependency: 
 implementation 'com.android.support:recyclerview-v7:28.0.2'     //Recycler view
    implementation 'com.squareup.picasso:picasso:2.5.2'                //For loading Images from https link
    implementation 'de.hdodenhof:circleimageview:2.1.0'                 //used to show image in circuler view 
    implementation 'com.android.volley:volley:1.0.0'                           //Used to fetch JSON Data from a link
    implementation 'com.android.support:design:28.0.0'

There are 3 Packages and 2 class without any package.

>>>> In main class 
1> Checking Inter Net Connectivity
2> setIdForAllWidget() method is used to set id of each widget.
4> Recycler view is used to show the which we getting from Github Retrofit JSON.
5> Also onClickEditable() is used here to search data in the recycler view.

>>> In Internet Package
1>Checking state of internet.
2>Showing customized alertDialog.

>>> Tab Package
Here we are using 1 class and 2 fragments and 2 adapters

>>Adapters
1> One for Tab
2> For Recycler view which used to showing repository details.
>>Class
Only One Tab class which is handling 
1> Internet connectivity
2> Tab adpter 

>>> In Packege Model 
we having 2 classes 
1>>class UsermodelProfile 
It is used to get JSON data which we are showing in recycler view of main activity.

2>>class UserModelRepository
It is used to get Json Data in Repository Tab fragment.





