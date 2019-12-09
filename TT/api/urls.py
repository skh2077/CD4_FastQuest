from django.urls import path, re_path
from . import views
from rest_framework.urlpatterns import format_suffix_patterns

app_name = 'api'

urlpatterns = [
    # http://127.0.0.1:8000/api
    path('', views.post_list),

    # http://127.0.0.1:8000/api/activity/
    path('activity/', views.activity_list),

    # http://127.0.0.1:8000/api/category/
    path('category/', views.CategoryList.as_view()),

    # http://127.0.0.1:8000/api/lcat/음악
    re_path(r'^lcat/(?P<lcat_name>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.smallcat),

    # http://127.0.0.1:8000/api/activity/1
    path('activity/<int:pk>', views.ActivityDetail.as_view()),

    # http://127.0.0.1:8000/api/category/1
    # path('category/<int:pk>/', views.category_detail),

    # http://127.0.0.1:8000/api/profiles/
    path('profiles/', views.UserList.as_view()),

    # http://127.0.0.1:8000/api/profiles/1
    path('profiles/<int:pk>', views.UserDetail.as_view()),

    # http://127.0.0.1:8000/api/detail/1
    path('detail/<int:pk>', views.ProfileDetail.as_view()),

    # http://127.0.0.1:8000/api/user/samsung
    re_path(r'^user/(?P<username>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.user_detail),

    # http://127.0.0.1:8000/api/users/기니피그
    re_path(r'^users/(?P<nickname>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.category_pick),

    # http://127.0.0.1:8000/api/activity/한식
    re_path(r'^activity/(?P<cat_name>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.activity_pick),

    # http://127.0.0.1:8000/api/feed/  # 전체 피드글 목록
    path('feed/', views.FeedView.as_view()),

    # http://127.0.0.1:8000/api/feed/1  # 피드글 하나
    path('feed/<int:pk>', views.FeedDetail.as_view()),

    # http://127.0.0.1:8000/api/feed/user/rbqbwbrb  # 해당 유저가 작성한 피드글 목록록
    re_path(r'^feed/user/(?P<name>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.FeedUser.as_view()),

    # http://127.0.0.1:8000/api/user/precat/  # 전체선호목록 GET, POST 용
    path('user/precat/', views.UserPrecat.as_view()),

    # http://127.0.0.1:8000/api/user/1/precat/  # 한사람 선호목록 GET, 한사람 목록 DELETE
    path('user/<int:pk>/precat/', views.UserEachPrecat.as_view()),

    # http://127.0.0.1:8000/api/assemble/  # 모든 모임 목록
    path('assemble/', views.AssembleList.as_view()),

    # http://127.0.0.1:8000/api/assemble/1  # 특정 모임 글
    path('assemble/<int:pk>', views.AssembleDetail.as_view()),

    # http://127.0.0.1:8000/api/select/rbqbwbrb
    re_path(r'^select/(?P<username>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$',
            views.OnlyInside.as_view()),

    # http://127.0.0.1:8000/api/select/rbqbwbrb/37.54/126.9  # 유저네임, 위도, 경도 받고 활동 추천. 토큰 있음 유저네임 제거도 가능.(request.user)
    re_path(r'^select/(?P<username>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)/(?P<latitude>\d{2}[.]\d+)/(?P<longitude>\d{3}[.]\d+)$',
            views.OutsideActivity.as_view()),

    # http://127.0.0.1:8000/api/selectassemble/rbqbwbrb
    re_path(r'^selectassemble/(?P<username>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$',
            views.assembleselect),

    # http://127.0.0.1:8000/api/challenge/  # 모든 도전 목록
    path('challenge/', views.ChallengeList.as_view()),

    # http://127.0.0.1:8000/api/challenge/1  # 특정 도전 글
    path('challenge/<int:pk>', views.ChallengeDetail.as_view()),

    # http://127.0.0.1:8000/api/likefeed/  # 피드 좋아요 목록
    path('likefeed/', views.LikeFeedList.as_view()),

    # http://127.0.0.1:8000/api/likefeed/1  # 특정 좋아요
    path('likefeed/<int:pk>', views.LikeFeedDetail.as_view()),

    # http://127.0.0.1:8000/api/likefeed/user/rbqbwbrb  # 해당 유저가 작성한 좋아요
    re_path(r'^likefeed/user/(?P<name>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.LikeFeedUser.as_view()),

    # http://127.0.0.1:8000/api/likeassemble/  # 모임 좋아요 목록
    path('likeassemble/', views.LikeAssembleList.as_view()),

    # http://127.0.0.1:8000/api/likeassemble/1  # 특정 모임 좋아요
    path('likeassemble/<int:pk>', views.LikeAssembleDetail.as_view()),

    # http://127.0.0.1:8000/api/likeassemble/user/rbqbwbrb  # 해당 유저가 작성한 모임 좋아요
    re_path(r'^likeassemble/user/(?P<name>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.LikeAssembleUser.as_view()),

    # http://127.0.0.1:8000/api/takes/  # 모든 참가 목록
    path('takes/', views.TakesList.as_view()),

    # http://127.0.0.1:8000/api/takes/1  # 특정 참가 글
    path('takes/<int:pk>', views.TakesDetail.as_view()),

    # http://127.0.0.1:8000/api/takes/user/rbqbwbrb  # 해당 유저가 작성한 참가글 목록
    re_path(r'^takes/user/(?P<name>[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\*]+)$', views.TakesUser.as_view()),


]
# http://127.0.0.1:8000/api/activity/5/99/5/99/outside/solo/
# re_path(r'^activity/(?P<soc_start>\d{1,2})/(?P<soc_end>\d{1,2})/(?P<act_start>\d{1,2})/(?P<act_end>\d{1,2})/outside/solo$', views.activity_pick_outside, name='act_solo_outside'),
urlpatterns = format_suffix_patterns(urlpatterns)