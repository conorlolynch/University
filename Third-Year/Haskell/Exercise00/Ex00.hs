module Ex00 where

name, idno, username :: String
name      =  "censored"  -- replace with your name
idno      =  "censored"    -- replace with your student id
username  =  "censored"   -- replace with your TCD username


declaration -- do not modify this
 = unlines
     [ ""
     , "@@@ This exercise is all my own work."
     , "@@@ Signed: " ++ name
     , "@@@ "++idno++" "++username
     ]

{- Modify everything below here as you see fit to ensure all tests pass -}

hello  =  "Hello World :-)"
