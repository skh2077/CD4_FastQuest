# Generated by Django 2.2.6 on 2019-11-11 22:44

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0010_auto_20191111_2142'),
    ]

    operations = [
        migrations.AlterField(
            model_name='takes',
            name='id',
            field=models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID'),
        ),
    ]