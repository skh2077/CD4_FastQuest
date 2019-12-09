# -*- coding:utf-8 -*-
from django.views.decorators.csrf import csrf_exempt
from rest_framework.decorators import api_view
from rest_framework import status
from rest_framework import generics
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.generics import get_object_or_404
from rest_framework.utils import json
from .serializers import *
from django.http import JsonResponse, HttpResponse
from rest_framework import viewsets
from django.utils import timezone


@api_view(['GET', 'POST'])
@csrf_exempt
def activity_list(request):
    if request.method == 'GET':
        activity = Activity.objects.all()
        serializer = ActivitySerializer(activity, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    elif request.method == 'POST':
        serializer = ActivitySerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# @api_view(['GET', 'POST'])
# @csrf_exempt
# def category_list(request):
#     if request.method == 'GET':
#         category = Category.objects.all()
#         serializer = CategorySerializer(category, many=True)
#         return Response(serializer.data, status=status.HTTP_200_OK)
#     elif request.method == 'POST':
#         serializer = CategorySerializer(data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class CategoryList(APIView):  # 함수형. 기능차이 없음. 추가로 믹스인 구현가능
    """
    list all categories  or create new one
    """
    def get(self, request, format=None):
        category = Category.objects.all()
        serializer = CategorySerializer(category, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = CategorySerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# @csrf_exempt
# @api_view(['GET', 'PUT', 'DELETE'])
# def activity_detail(request, pk, format=None): # CRUD 확인용
#     activity = get_object_or_404(Activity, pk=pk)
#     if request.method == 'GET':
#         serializer = ActivitySerializer(activity)
#         return JsonResponse(serializer.data, safe=False, json_dumps_params = {'ensure_ascii': False})
#     elif request.method == 'PUT':
#         serializer = ActivitySerializer(activity, data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
#     elif request.method == 'DELETE':
#         activity.delete()
#         return HttpResponse(status=204)


class ActivityDetail(generics.RetrieveUpdateDestroyAPIView):  # 믹스인까지 다 합친경우. get, put, delete
    """
        retrieve, update, delete a blog
        https://www.django-rest-framework.org/tutorial/3-class-based-views/
    """
    queryset = Activity.objects.all()
    serializer_class = ActivitySerializer


# @csrf_exempt
# @api_view(['GET', 'PUT', 'DELETE'])
# def category_detail(request, pk): # RUD
#     category = get_object_or_404(Category, pk=pk)
#     if request.method == 'GET':
#         serializer = CategorySerializer(category)
#         return JsonResponse(serializer.data, safe=False, json_dumps_params = {'ensure_ascii': False})
#     elif request.method == 'PUT':
#         serializer = CategorySerializer(category, data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
#     elif request.method == 'DELETE':
#         category.delete()
#         return HttpResponse(status=204)


def category_pick(request, nickname):
    if request.method == 'GET':
        user = get_object_or_404(Profile, nickname=nickname)
        precat = PreCat.objects.filter(user=user).values_list('cat_name', flat=True)
        score = user.score
        act_range = score_to_activity(score)
        usercat = Category.objects.filter(cat_name__in=precat)
        category = Category.objects.filter(
            activity_rate__range=(user.activity-act_range, user.activity+act_range)
        )|usercat
        category = category.order_by('?').values('cat_name', 'activity_rate')[:5]  # serialize 대신 값들만 json화
        return JsonResponse(list(category), safe=False, json_dumps_params={'ensure_ascii': False})


def activity_pick(request, cat_name):  # 점수, 선호카테고리, 거리 알고리즘 추가할 것
    if request.method == 'GET':
        activity = Activity.objects.filter(category_id=cat_name)
        serializer = ActivitySerializer(activity, many=True)
        return HttpResponse(json.dumps(serializer.data, ensure_ascii=False),
                            content_type="application/json")


class OutsideActivity(APIView):
    def get(self, request, username, latitude, longitude):
        user = User.objects.get(username=username)
        precat = PreCat.objects.filter(user__id=user.id).values_list('cat_name', flat=True)
        usercat = Category.objects.filter(cat_name__in=precat)  # user 선호카테고리
        activity = user.profile.activity
        score = user.profile.score
        act_range = score_to_activity(score)
        cat = Category.objects.filter(
            activity_rate__range=(activity - act_range, activity + act_range),
        )|usercat
        category = cat.order_by('?').values_list('cat_name', flat=True)[:5]
        res = []
        for row in category:
            act = Activity.objects.filter(category=row).order_by('?')[:1]
            while not act.exists():
                try:
                    cat = Category.objects.filter(
                        activity_rate__range=(activity - act_range, activity + act_range),
                    ).order_by('?').values_list('cat_name', flat=True)[:1]
                    act = Activity.objects.filter(category=cat[0]).order_by('?')[:1]
                except BaseException as e:
                    print("No activity! {}\n".format(e))
            if act[0].outside == 'y':
                act2 = Activity.objects.raw(
                    "select distinct * from activity where title=%s order by sqrt(POW(latitude-%s, 2)+POW(longitude-%s, 2)) limit 1",
                    [act[0].title, latitude, longitude])[:1]
                res.append(act2[0])
            else:
                res.append(act[0])
        print(res)
        serializer = ActivitySerializer(res, many=True)
        return Response(serializer.data)


class OnlyInside(APIView):
    def get(self, request, username):
        user = User.objects.get(username=username)
        precat = PreCat.objects.filter(user__id=user.id).values_list('cat_name', flat=True)
        usercat = Category.objects.filter(cat_name__in=precat)  # user 선호카테고리
        activity = user.profile.activity
        score = user.profile.score
        act_range = score_to_activity(score)
        cat = Category.objects.filter(
            activity_rate__range=(activity - act_range, activity + act_range),
        )|usercat
        category = cat.order_by('?').values_list('cat_name', flat=True)[:5]
        res = []
        i=2
        for row in category:
            act = Activity.objects.filter(category=row, outside__isnull=True).order_by('?')[:1]
            while not act.exists():
                try:
                    cat = Category.objects.filter(
                        activity_rate__range=(activity - i*act_range, activity + i*act_range),
                    ).order_by('?').values_list('cat_name', flat=True)[:1]
                    i = i+1
                    act = Activity.objects.filter(category=cat[0], outside__isnull=True).order_by('?')[:1]
                except BaseException as e:
                    print("No activity! {}\n".format(e))
            else:
                res.append(act[0])
        serializer = InsideActivity(res, many=True)
        return Response(serializer.data)


@csrf_exempt
@api_view(['GET', 'POST'])
def user_detail(request, username):
    user = get_object_or_404(Profile, user__username=username)
    if request.method == 'GET':
        serializer = ProfileSerializer(user)
        return Response(serializer.data)
        # return HttpResponse(json.dumps(serializer.data, ensure_ascii=False),
        #                     content_type="application/json")
    if request.method == 'POST':
        serializer = ProfileSerializer(user, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer  # ProfileSerializer에서 바꿈


def post_list(request):
    print(request.user)
    return JsonResponse({
        'message': 'Capstone Design Team4',
        'items': ['패스트', '퀘스트', 'AWS', 'ELB'],
    }, json_dumps_params={'ensure_ascii': False})


# class UserList(generics.ListAPIView): # JSON
#     queryset = User.objects.all()
#     serializer_class = UserOriginSerializer
#
#     class Meta:  # Unordered Error 방지
#         ordering = ['-id']
#
#     def perform_create(self, serializer):  # 코드에 연결 : 인증
#         serializer.save(user=self.request.user)


class UserList(APIView):  # JSONArray
    def get(self, request, format=None):
        users = User.objects.all()
        serializer = UserSerializer(users, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = UserOriginSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class UserDetail(generics.RetrieveAPIView):  # 읽기전용으로 유저 한명 정보
    queryset = User.objects.all()
    serializer_class = UserSerializer


class ProfileDetail(generics.RetrieveUpdateDestroyAPIView):  # 유저 프로필항목 수정을 위한
    queryset = Profile.objects.all()
    serializer_class = ProfileSerializer


class FeedView(APIView):
    def get(self, request, format=None):
        feed = Feed.objects.all()
        serializer = FeedViewSerializer(feed, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = FeedSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class FeedDetail(APIView): #
    def get_object(self, pk):
        feed = get_object_or_404(Feed, pk=pk)
        return feed

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = FeedViewSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = FeedPutSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class FeedUser(APIView):
    def get(self, request, name):
        feed = Feed.objects.filter(author__user__username=name)
        serializer = FeedViewSerializer(feed, many=True)
        return Response(serializer.data)


class UserPrecat(APIView):
    def get(self, request, format=None):
        precat = PreCat.objects.all()
        serializer = PrecatSerializer(precat, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = PrecatSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class UserEachPrecat(APIView):
    def get(self, request, pk):
        precat = PreCat.objects.filter(user=pk)
        serializer = PrecatSerializer(precat, many=True)
        return Response(serializer.data)

    def delete(self, request, pk, format=None):
        snippet = PreCat.objects.filter(user=pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


# 대카테고리 입력시 소카테고리들 출력
def smallcat(request, lcat_name):  # 점수, 선호카테고리, 거리 알고리즘
    if request.method == 'GET':
        category = Category.objects.filter(lcat_name_id__lcat_name__startswith=lcat_name)
        serializer = CategorySerializer(category, many=True)
        return HttpResponse(json.dumps(serializer.data, ensure_ascii=False),
                            content_type="application/json")


# 만든 모임
class AssembleList(APIView):
    def get(self, request):
        assemble = Assemble.objects.all()
        serializer = AssembleSerializer(assemble, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = AssembleSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class AssembleDetail(APIView):
    def get_object(self, pk):
        assemble = get_object_or_404(Assemble, pk=pk)
        return assemble

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = AssembleSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = AssembleSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


# 모임 선택
@api_view(['GET'])
def assembleselect(request, username):
    if request.method == 'GET':
        user = User.objects.get(username=username)
        precat = PreCat.objects.filter(user__id=user.id).values_list('cat_name', flat=True)
        usercat = Category.objects.filter(cat_name__in=precat) # user 선호카테고리
        activity = user.profile.activity
        score = user.profile.score
        act_range = score_to_activity(score)
        category = Category.objects.filter(
            activity_rate__range=(activity - act_range, activity + act_range),
        )|usercat
        category = category.values_list('cat_name', flat=True)
        category = list(category)
        today, time = str(timezone.now()).split(' ')
        year, month, day = today.split('-')
        day = int(day)
        hour, min, sec = time.split(':')
        hour = int(hour)
        assemble = Assemble.objects.filter(time__gt=timezone.now(),
                                           time__year=year,
                                           time__month=month,
                                           time__day__lte=day+1,
                                           # time__hour__range=(hour, hour+24),
                                           category_id__in=category,
                                           ).order_by('?', 'time')
        serializer = AssembleComplexSerializer(assemble, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)


# '도전!' 목록
class ChallengeList(APIView):
    def get(self, request):
        challenge = Challenge.objects.all()
        serializer = ChallengeSerializer(challenge, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = ChallengeSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ChallengeDetail(APIView):
    def get_object(self, pk):
        challenge = get_object_or_404(Challenge, pk=pk)
        return challenge

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = ChallengeSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = ChallengeSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


# 완료한 활동들
class TakesList(APIView):
    def get(self, request):
        takes = Takes.objects.all()
        serializer = TakesSerializer(takes, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):  # 번호를 이름으로 저장하고 이름을 번호로 주기
        user = get_object_or_404(User, username=request.data['user'])
        _mutable = request.data._mutable
        request.data._mutable = True
        request.data['user'] = user.id
        request.data._mutable = _mutable
        serializer = TakesPostSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class TakesUser(APIView):
    def get(self, request, name):
        takes = Takes.objects.filter(user__user__username=name)
        serializer = TakesSerializer(takes, many=True)
        return Response(serializer.data)


class TakesDetail(APIView):
    def get_object(self, pk):
        takes = get_object_or_404(Takes, pk=pk)
        return takes

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = TakesSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = TakesSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


# 피드 좋아요
class LikeFeedList(APIView):
    def get(self, request):
        likefeed = LikeFeed.objects.all()
        serializer = LikeFeedSerializer(likefeed, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        user = get_object_or_404(User, username=request.data['user'])
        _mutable = request.data._mutable
        request.data._mutable = True
        request.data['user'] = user.id
        request.data._mutable = _mutable
        serializer = LikeFeedPostSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class LikeFeedDetail(APIView):
    def get_object(self, pk):
        likefeed = get_object_or_404(LikeFeed, pk=pk)
        return likefeed

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = LikeFeedSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = LikeFeedSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class LikeFeedUser(APIView):
    def get(self, request, name):
        likefeed = LikeFeed.objects.filter(user__user__username=name)
        serializer = LikeFeedSerializer(likefeed, many=True)
        return Response(serializer.data)


# 모임임 좋아요
class LikeAssembleList(APIView):
    def get(self, request):
        likeassemble = LikeAssemble.objects.all()
        serializer = LikeAssembleSerializer(likeassemble, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        user = get_object_or_404(User, username=request.data['user'])
        _mutable = request.data._mutable
        request.data._mutable = True
        request.data['user'] = user.id
        request.data._mutable = _mutable
        serializer = LikeAssemblePostSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class LikeAssembleDetail(APIView):
    def get_object(self, pk):
        likeassemble = get_object_or_404(LikeAssemble, pk=pk)
        return likeassemble

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = LikeAssembleSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = LikeAssembleSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class LikeAssembleUser(APIView):
    def get(self, request, name):
        likeassemble = LikeAssemble.objects.filter(user__user__username=name)
        serializer = LikeAssembleSerializer(likeassemble, many=True)
        return Response(serializer.data)


def score_to_activity(score):
    if score < 100:
        return 10
    elif 100 <= score < 500:
        return 20
    elif 500 <= score < 1000:
        return 30
    elif 1000 <= score < 2000:
        return 40
    elif 2000 <= score < 5000:
        return 50
    else:
        return 60
