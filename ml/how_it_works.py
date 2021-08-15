#!/usr/bin/env python
# coding: utf-8

# In[257]:


import joblib
import pandas as pd
import string
import sys


# In[258]:


model = joblib.load('model3.py')


# In[259]:


if __name__ == "__main__":
    for param in sys.argv:
        host = param


# In[260]:


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


# In[265]:


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
    
    #колонки с цифрами
    df['len_host'] = df.hosts.apply(lambda x: len(x))
    df['len_end'] = df.end.apply(lambda x: len(x))
    try: 
        df['len_main_word'] = df.main_word.apply(lambda x: len(x))
    except:
        df['len_main_word'] = None
    
    df['share_of_end'] = df.len_end / df.len_host
    df['share_of_main_word'] = df.len_main_word / df.len_host
    df['share_of_podhost'] = 1 - df['share_of_end'] - df['share_of_main_word']
    
    df['relation_of_podhost_to_main_word'] = df['len_host'] / df['len_main_word']
    
    df['qnt_words'] = df.hosts_list.apply(lambda x: len(x))
    
    df['letters_in_host'] = df.char_in_host.apply(lambda x: x[0])
    df['numbers_in_host'] = df.char_in_host.apply(lambda x: x[1])
    df['symbols_in_host'] = df.char_in_host.apply(lambda x: x[2])
    
    df['share_of_letters_in_host'] = df.letters_in_host / df.len_host
    df['share_of_numbers_in_host'] = df.numbers_in_host / df.len_host
    df['share_of_symbols_in_host'] = df.symbols_in_host / df.len_host
    
    df['numbers_in_end'] = df.hosts.apply(lambda x: check_char(x)[1])
    
    return df


# In[266]:


df = make_def(host)


# In[267]:


df = preparing_data(df)

features = ['len_host', 'len_end', 'len_main_word', 'share_of_end', 'share_of_main_word', 'share_of_podhost', 'qnt_words', 'letters_in_host',             'numbers_in_host', 'symbols_in_host', 'share_of_letters_in_host',             'share_of_numbers_in_host', 'relation_of_podhost_to_main_word', 'share_of_symbols_in_host', 'numbers_in_end']

cat_features = ['hosts', 'hosts_text', 'end', 'main_word']

# 
x = df[features]
x_cat = x.join(df[cat_features])
x_cat.shape


# In[268]:


# делаем предсказание
prediction = model.predict(x_cat)
# 1 - тех
# 0 - нетех


# In[269]:


# вероятности отнесения к классам
prob_0, prob_1 = model.predict_proba(x_cat)[0]


# In[270]:


if prediction == 0:
    print('пользовательская страница, уверенность прогноза:', f'{round(prob_0*100, 2)}%')
else:
    print('техническая страница, уверенность прогноза: ', f'{round(prob_1*100, 2)}%')


# In[ ]:





# In[ ]:




