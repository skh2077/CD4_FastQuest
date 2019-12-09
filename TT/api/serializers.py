from .models import *
from rest_framework import serializers
from rest_auth.serializers import UserDetailsSerializer
from django.contrib.auth.models import User


class ActivitySerializer(serializers.ModelSerializer):
    class Meta:
        model = Activity
        fields = ['act_id', 'title', 'category', 'content', 'longterm', 'outside', 'address', 'latitude', 'longitude']


class OutsideActivity(serializers.ModelSerializer):
    cat_name = serializers.CharField(source='category')
    activity_rate = serializers.IntegerField(source='category.activity_rate')
    class Meta:
        model = Activity
        fields = ['act_id', 'title', 'cat_name', 'content', 'longterm', 'outside', 'address', 'latitude', 'longitude', 'activity_rate']


class InsideActivity(serializers.ModelSerializer):
    cat_name = serializers.CharField(source='category')
    activity_rate = serializers.IntegerField(source='category.activity_rate')
    class Meta:
        model = Activity
        fields = ['act_id', 'title', 'cat_name', 'content', 'longterm', 'outside', 'address', 'latitude', 'longitude', 'activity_rate']


class ActivityPutSerializer(serializers.ModelSerializer): # Response 타입이 달라짐. 사용은 가능 PUT시에
    class Meta:
        model = Activity
        fields = ['title', 'category', 'content']


class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = ['cat_name', 'lcat_name', 'activity_rate', 'sociality_rate']


class ChallengeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Challenge
        fields = '__all__'


class ProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Profile
        exclude = ['user']


class AssembleSerializer(serializers.ModelSerializer):
    class Meta:
        model = Assemble
        fields = ['id', 'title', 'category', 'content', 'time', 'photo', 'author']


class AssembleComplexSerializer(serializers.ModelSerializer):
    activity_rate = serializers.IntegerField(source='category.activity_rate')
    cat_name = serializers.CharField(source='category_id')
    class Meta:
        model = Assemble
        fields = ['id', 'title', 'cat_name','content', 'time', 'photo', 'author', 'activity_rate']


class TakesSerializer(serializers.ModelSerializer):
    username = serializers.CharField(source='user.user.username')
    nickname = serializers.CharField(source='user.nickname')
    class Meta:
        model = Takes
        fields = ['id', 'user', 'username', 'nickname', 'act', 'star', 'date',]


class TakesPostSerializer(serializers.ModelSerializer):
    class Meta:
        model = Takes
        fields = '__all__'


class PrecatSerializer(serializers.ModelSerializer):
    class Meta:
        model = PreCat
        exclude = ('id',)


class LargecatSerializer(serializers.ModelSerializer):
    class Meta:
        model = LargeCat
        fields = '__all__'


class FeedSerializer(serializers.ModelSerializer):
    class Meta:
        model = Feed
        fields = '__all__'


class FeedViewSerializer(serializers.ModelSerializer):
    username = serializers.CharField(source='author.user.username')
    nickname = serializers.CharField(source='author.nickname')
    class Meta:
        model = Feed
        fields = ['id', 'title', 'content', 'time', 'image', 'act', 'username', 'nickname', 'author']



class FeedPutSerializer(serializers.ModelSerializer):
    class Meta:
        model = Feed
        exclude = ('author', 'act', 'time')

# class CustomProfileSerializer(serializers.ModelSerializer):  # 아래에 넣기 위한
#     user = UserDetailsSerializer()
#
#     class Meta:
#         model = Profile
#         fields = ('user',)


# https://django-rest-auth.readthedocs.io/en/latest/faq.html
# https://stackoverflow.com/questions/53883177/overriding-the-userdetailsserializer-in-django-rest-auth
class UserSerializer(UserDetailsSerializer):  # Rest-auth. 프로필 시리얼라이저로 삽입. UserModel을 참조하는 디테일 시리얼라이저를 상속
    # profile = ProfileSerializer('profile')  # profile 모델을 통째로 가져온다.
    # profile = ProfileSerializer(source='profile') # profile과 이름이 같아 source=을 제거해줘도 된다.
    # nickname = serializers.CharField(source="profile.nickname")
    # 오버라이딩 하거나 정의
    nickname = serializers.CharField(source='profile.nickname')
    score = serializers.IntegerField(source='profile.score')
    activity = serializers.IntegerField(source='profile.activity')
    sociality = serializers.IntegerField(source='profile.sociality')
    gender = serializers.CharField(source='profile.gender')
    age = serializers.IntegerField(source='profile.age')

    class Meta(UserDetailsSerializer.Meta):
        fields = ['id', 'username', 'email', 'nickname', 'score', 'activity', 'sociality', 'gender', 'age', 'last_login', 'date_joined']
        # fields = '__all__'  #['email', 'profile']
        # fields = UserDetailsSerializer.Meta.fields + ('profile',)  # 메타 정보 상속받는 방식
        # read_only_fields = ('',)  # 읽기 전용인 필드는 현재 없음

    # def update(self, instance, validated_data):  #  업데이트는 프로필에서 대체. 공식 업데이트 정의방식.
    #     profile_data = validated_data.pop('profile', {})
    #     nickname = profile_data.get('nickname')
    #
    #     instance = super(UserSerializer, self).update(instance, validated_data)
    #
    #     # get and update user profile
    #     prof = instance.profile
    #     if profile_data and nickname:
    #         prof.company_name = nickname
    #         prof.save()
    #     return instance


class UserOriginSerializer(serializers.ModelSerializer):  # 4 진행 http://raccoonyy.github.io/drf3-tutorial-4/
    profile = serializers.PrimaryKeyRelatedField(queryset=Profile.objects.all())
    # many=True 제거 https://django-rest-auth.readthedocs.io/en/latest/faq.html
    # user = serializers.ReadOnlyField(source='user.username')

    class Meta:
        model = User
        fields = ('id', 'username', 'profile', )  # 'user'


# class RegisterSerializer(RegisterSerializer):  # 안먹혀서 봉인. 유저네임을 닉네임으로
#     nickname = serializers.CharField()
#
#     class Meta(UserDetailsSerializer.Meta):
#         fields = ['username, emial, password1', 'nickname']
#
#     def get_cleaned_data(self):
#         return {
#             'username': self.validated_data.get('username', ''),
#             'password1': self.validated_data.get('password1', ''),
#             'email': self.validated_data.get('email', ''),
#             'nickname': self.validated_data.get('nickname', ''),
#         }
#
#     def save(self, request):
#         self.cleaned_data = self.get_cleaned_data()
#         user = super(RegisterSerializer, self).save()
#
#         user.profile.nickname = self.cleaned_data.get('nickname')
#         user.save()
#         return user


class LikeFeedPostSerializer(serializers.ModelSerializer):
    class Meta:
        model = LikeFeed
        fields = '__all__'


class LikeFeedSerializer(serializers.ModelSerializer):
    username = serializers.CharField(source='user.user.username')
    class Meta:
        model = LikeFeed
        fields = ['id', 'username', 'feed',]


class LikeAssemblePostSerializer(serializers.ModelSerializer):
    class Meta:
        model = LikeAssemble
        fields = '__all__'


class LikeAssembleSerializer(serializers.ModelSerializer):
    username = serializers.CharField(source='user.user.username')
    class Meta:
        model = LikeAssemble
        fields = ['id', 'username', 'assemble',]