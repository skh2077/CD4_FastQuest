# Generated by Django 2.2.6 on 2019-11-19 03:12

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0020_auto_20191119_0311'),
    ]

    operations = [
        migrations.AlterField(
            model_name='profile',
            name='sociality',
            field=models.IntegerField(blank=True, default=100, null=True),
        ),
    ]