# Generated by Django 2.2.6 on 2019-12-03 02:16

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0023_challenge'),
    ]

    operations = [
        migrations.CreateModel(
            name='LikeFeed',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('like', models.IntegerField(default=0)),
                ('feed', models.ForeignKey(on_delete=django.db.models.deletion.DO_NOTHING, to='api.Feed')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.DO_NOTHING, to='api.Profile')),
            ],
            options={
                'db_table': 'likefeed',
                'unique_together': {('id', 'feed')},
            },
        ),
    ]