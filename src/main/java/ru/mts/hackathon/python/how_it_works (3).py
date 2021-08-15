#!/usr/bin/env python
# coding: utf-8

# In[11]:


import joblib
import pandas as pd
import string

model = joblib.load('src/main/java/ru/mts/hackathon/python/model3.py')


# In[13]:


host = input()


# In[14]:


def make_def(string):
    df = pd.DataFrame({'hosts':[string]})
    return df

def check_char(row):
    letter = 0
    number = 0
    symb = 0
    for el in row:
        if el.lower() in string.ascii_lowercase:
            letter += 1
        elif el in [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]:
            number += 1
        else:
            symb += 1
    return letter, number, symb

def def_freq_word(row, y_list):
    if row in y_list:
        return 1
    else:
        return 0
    
def encoding(row):
    if row == 'teh':
        return 1
    else:
        return 0


# In[16]:


def preparing_data(df):

    # текстовые колонки
    df['hosts_text'] = df.hosts.apply(lambda x: ' '.join(x.split('.')))
    df['hosts_list'] = df.hosts.apply(lambda x: x.split('.'))
    df['end'] = df.hosts_list.apply(lambda x: x[-1])
    df['without_end'] = df.hosts_list.apply(lambda x: x[:-1])
    try:
        df['main_word'] = df.without_end.apply(lambda x: x[-1])
    except:
        df['main_word'] = None
    
    df['char_in_host'] = df.hosts.apply(lambda x: check_char(x))
    
    freq_words = df['main_word'].value_counts()
    freq_words = freq_words[freq_words > 1]
    freq_words_list = freq_words.index
    
    freq_ends = df.end.value_counts()
    freq_ends = freq_ends[freq_ends > 1]
    freq_ends_list = freq_ends.index
    
    #колонки с цифрами
    df['len_host'] = df.hosts.apply(lambda x: len(x))
    df['len_end'] = df.end.apply(lambda x: len(x))
    df['len_main_word'] = df.main_word.apply(lambda x: len(x))
    
    df['share_of_end'] = df.len_end / df.len_host
    df['share_of_main_word'] = df.len_main_word / df.len_host
    df['share_of_podhost'] = 1 - df['share_of_end'] - df['share_of_main_word']
    
    df['qnt_words'] = df.hosts_list.apply(lambda x: len(x))
    
    df['letters_in_host'] = df.char_in_host.apply(lambda x: x[0])
    df['numbers_in_host'] = df.char_in_host.apply(lambda x: x[1])
    df['symbols_in_host'] = df.char_in_host.apply(lambda x: x[2])
    
    df['share_of_letters_in_host'] = df.letters_in_host / df.len_host
    df['share_of_numbers_in_host'] = df.numbers_in_host / df.len_host
    df['share_of_symbols_in_host'] = df.symbols_in_host / df.len_host
    
    df['numbers_in_end'] = df.hosts.apply(lambda x: check_char(x)[1])
    
    df['freq_word'] = df.main_word.apply(lambda x: def_freq_word(x, freq_words_list))
    df['freq_end'] = df.main_word.apply(lambda x: def_freq_word(x, freq_ends_list))
    
    return df


# In[17]:


df = make_def(host)


# In[18]:


df = preparing_data(df)

features = ['len_host', 'len_end', 'len_main_word', 'share_of_end', 'share_of_main_word', 'share_of_podhost', 'qnt_words', 'letters_in_host',             'numbers_in_host', 'symbols_in_host', 'share_of_letters_in_host',             'share_of_numbers_in_host', 'share_of_symbols_in_host', 'numbers_in_end', 'freq_word', 'freq_end']

cat_features = ['hosts', 'hosts_text', 'end', 'main_word']

x = df[features]
x_cat = x.join(df[cat_features])
x_cat.shape


# In[19]:


# делаем предсказание
prediction = model.predict(x_cat)
# 1 - пользовательский
# 0 - технический


# In[20]:


# вероятности отнесения к классам
prob_0, prob_1 = model.predict_proba(x_cat)[0]


# In[21]:


if prediction == 1:
    print('Пользовательский, уверенность прогноза:', f'{round(prob_1*100, 2)}%')
else:
    print('Технический, уверенность прогноза: ', f'{round(prob_0*100, 2)}%')


# In[ ]:





# In[ ]:




