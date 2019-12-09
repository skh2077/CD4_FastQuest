from django.utils.timezone import now
from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver
import datetime


class LargeCat(models.Model):
    lcat_name = models.CharField(db_column='Lcat_name', primary_key=True, max_length=50)  # Field name made lowercase.

    class Meta:
        db_table = 'large_cat'

    def __str__(self):
        return self.lcat_name


class Category(models.Model):
    cat_name = models.CharField(primary_key=True, max_length=50)
    lcat_name = models.ForeignKey(LargeCat, models.DO_NOTHING, db_column='Lcat_name', blank=True, null=True)  # Field name made lowercase.
    activity_rate = models.IntegerField(blank=True, null=True)
    sociality_rate = models.IntegerField(blank=True, null=True)

    class Meta:
        db_table = 'category'

    def __str__(self):
        return self.cat_name


class Activity(models.Model):
    act_id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    category = models.ForeignKey(Category, models.DO_NOTHING, db_column='category')
    content = models.TextField(blank=True, null=True)
    longterm = models.CharField(max_length=1, blank=True, null=True)
    outside = models.CharField(max_length=1, blank=True, null=True)
    address = models.CharField(max_length=100, blank=True, null=True)
    latitude = models.FloatField(blank=True, null=True)
    longitude = models.FloatField(blank=True, null=True)

    class Meta:
        db_table = 'activity'

    def __str__(self):
        return '%s. %s' % (self.act_id, self.title)


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    nickname = models.CharField(max_length=30, blank=True, null=True)
    score = models.IntegerField(default=0, blank=True, null=True)
    activity = models.IntegerField(blank=True, null=True)
    sociality = models.IntegerField(default=100, blank=True, null=True)
    gender = models.CharField(default='M', max_length=10, blank=True, null=True)
    age = models.IntegerField(blank=True, null=True)
    created = models.DateTimeField(default=now, blank=True, null=True)

    class Meta:
        db_table = 'profile'

    def __str__(self):
        return '%s %s' % (self.user.username, self.user.email)
        # return self.user.username


@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        Profile.objects.create(user=instance)


@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.profile.save()


class Assemble(models.Model):
    title = models.CharField(max_length=50, blank=True, null=True)
    category = models.ForeignKey(Category, models.DO_NOTHING, blank=True, null=True)
    content = models.TextField(blank=True, null=True)
    time = models.DateTimeField(default=now, blank=True, null=True)
    photo = models.ImageField(default='default_image.jpeg')
    author = models.ForeignKey(Profile, models.DO_NOTHING, blank=True, null=True)

    class Meta:
        db_table = 'assemble'

    def __str__(self):
        return self.title


class Feed(models.Model):
    act = models.ForeignKey(Activity, models.DO_NOTHING)
    title = models.CharField(max_length=50, blank=True, null=True)
    content = models.TextField(blank=True, null=True)
    time = models.DateTimeField(default=now, blank=True, null=True)
    author = models.ForeignKey(Profile, models.DO_NOTHING, blank=True, null=True)
    image = models.ImageField(default='default_image.jpeg')

    class Meta:
        db_table = 'feed'

    def __str__(self):
        return '%s. %s' % (self.act, self.title)


class PreCat(models.Model):
    #id = models.AutoField(primary_key=True)
    user = models.ForeignKey(Profile, models.DO_NOTHING, db_column='user_id')
    cat_name = models.ForeignKey(Category, models.DO_NOTHING, db_column='cat_name')

    class Meta:
        db_table = 'pre_cat'
        unique_together = (('user', 'cat_name'),)

    def __str__(self):
        return '%s: %s' % (self.user, self.cat_name)



class Takes(models.Model):
    user = models.ForeignKey(Profile, models.DO_NOTHING, db_column='userid')
    act = models.ForeignKey(Activity, models.DO_NOTHING)
    star = models.IntegerField(default=3, blank=True, null=True)
    date = models.DateField(default=datetime.date.today)

    class Meta:
        db_table = 'takes'
        unique_together = (('id', 'act', 'date'),)

    def __str__(self):
        return '%s. %s' % (self.user.user.username, self.act)

class Challenge(models.Model):
    cat_name = models.CharField(max_length=50)
    act_name = models.CharField(max_length=50)
    content = models.TextField(blank=True, null=True)
    image = models.ImageField(default='default_image.jpeg')

    def __str__(self):
        return '%s. %s' % (self.cat_name, self.act_name)


class LikeFeed(models.Model):
    user = models.ForeignKey(Profile, models.DO_NOTHING)
    feed = models.ForeignKey(Feed, models.DO_NOTHING)

    class Meta:
        db_table = 'likefeed'
        unique_together = (('id', 'feed'),)

    def __str__(self):
        return '%s. %s' % (self.user.user.username, self.feed)


class LikeAssemble(models.Model):
    user = models.ForeignKey(Profile, models.DO_NOTHING)
    assemble = models.ForeignKey(Assemble, models.DO_NOTHING)

    class Meta:
        db_table = 'likeassemble'
        unique_together = (('id', 'assemble'),)

    def __str__(self):
        return '%s. %s' % (self.user.user.username, self.assemble)