package com.home.englishnote.presenters;


import com.home.englishnote.exceptions.EmailFormatInvalidException;
import com.home.englishnote.exceptions.InvalidCredentialsException;
import com.home.englishnote.exceptions.MemberIdInvalidException;
import com.home.englishnote.exceptions.MemberInfoNotFoundException;
import com.home.englishnote.exceptions.PasswordFormatInvalidException;
import com.home.englishnote.exceptions.UserInputEmptyException;
import com.home.englishnote.models.entities.Credentials;
import com.home.englishnote.models.entities.Member;
import com.home.englishnote.models.entities.Token;
import com.home.englishnote.models.repositories.MemberRepository;
import com.home.englishnote.views.BaseView;
import com.home.englishnote.utils.HashUtil;
import com.home.englishnote.utils.ThreadExecutor;
import com.home.englishnote.utils.VerifyInputFormatUtil;


public class SignInPresenter {

    private SignInView signInView;
    private MemberRepository memberRepository;
    private ThreadExecutor threadExecutor;

    public SignInPresenter(SignInView signInView,
                           MemberRepository memberRepository,
                           ThreadExecutor threadExecutor) {
        this.signInView = signInView;
        this.memberRepository = memberRepository;
        this.threadExecutor = threadExecutor;
    }

    public void signIn(String email, String password, String passwordSalt) {
        threadExecutor.execute(() -> {
            try {
//                verifyUserInfo(email, password);
                String hashPassword = HashUtil.hashEncode(password, passwordSalt.getBytes());
                Credentials credentials = new Credentials(email, hashPassword);
                Token token = memberRepository.signInToken(credentials);
                int memberId = token.getMemberId();
                Member member = memberRepository.getMember(memberId);
                threadExecutor.executeUiThread(
                        () -> signInView.onSignInSuccessfully(member));
            } catch (UserInputEmptyException err) {
                threadExecutor.executeUiThread(signInView::onUserInputEmpty);
            } catch (EmailFormatInvalidException err) {
                threadExecutor.executeUiThread(signInView::onEmailFormatInvalid);
            } catch (PasswordFormatInvalidException err) {
                threadExecutor.executeUiThread(signInView::onPasswordFormatInvalid);
            } catch (InvalidCredentialsException err) {
                threadExecutor.executeUiThread(signInView::onInvalidCredentials);
            } catch (MemberIdInvalidException err) {
                err.printStackTrace();
            } catch (MemberInfoNotFoundException err) {
                err.printStackTrace();
            }
        });
    }

    private void verifyUserInfo(String email, String password) {
        //Todo
        VerifyInputFormatUtil.verifyInputEmpty(email, password);
        VerifyInputFormatUtil.verifyEmailFormat(email);
        VerifyInputFormatUtil.verifyPasswordFormat(password);
    }

    public interface SignInView extends BaseView {

        void onInvalidCredentials();

        void onSignInSuccessfully(Member member);
    }
}
